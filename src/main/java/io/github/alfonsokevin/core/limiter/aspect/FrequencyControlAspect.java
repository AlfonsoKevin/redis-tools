package io.github.alfonsokevin.core.limiter.aspect;


import io.github.alfonsokevin.core.base.exception.code.FreqResultCode;
import io.github.alfonsokevin.core.base.exception.code.StandardResultCode;
import io.github.alfonsokevin.core.base.exception.impl.FrequencyControlException;
import io.github.alfonsokevin.core.config.RedisToolsAutoConfiguration;
import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.base.exception.impl.FrequencyControlBuilder;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import io.github.alfonsokevin.core.limiter.strategy.key.factory.KeyStrategyFactory;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import io.github.alfonsokevin.core.limiter.strategy.limit.factory.FrequencyControlStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Order(1)
@Aspect
@Component
// 指定在其他自动配置类应用之后启动
@AutoConfigureAfter(RedisToolsAutoConfiguration.class)
@RequiredArgsConstructor
public class FrequencyControlAspect {

    private static final Logger log = LoggerFactory.getLogger(FrequencyControlAspect.class);

    @Autowired
    @Qualifier("redissonToolsClient")
    private RedissonClient redissonClient;


    private final FrequencyControlStrategyFactory limiterTypeStrategyFactory;
    private final KeyStrategyFactory keyStrategyFactory;

    @PostConstruct
    public void init() {
        if (Objects.isNull(redissonClient)) {
            log.warn("[{FrequencyControl}]: >> Startup failed, please check Redis configuration");
        }
        log.info("[{FrequencyControl}]: >> The project has been successfully launched ~~ redissonClient:{}"
                , redissonClient.getConfig());
    }

    @Pointcut("@annotation(frequencyControl)")
    public void pointcutLimit(FrequencyControl frequencyControl) {

    }

    @Around("pointcutLimit(frequencyControl)")
    public Object around(ProceedingJoinPoint joinPoint, FrequencyControl frequencyControl) throws Throwable {
        // 1.如果不存在rateLimiter直接返回
        if (Objects.isNull(frequencyControl)) {
            // 方法上没有添加注解
            log.error("[{FrequencyControl}]: >> No annotations have been added to the method");
            throw new FrequencyControlException(
                    "[{FrequencyControl}]: >> The FrequencyControl will not be able to be used"
                    , StandardResultCode.INITIALIZATION_ERROR.getCode());
        }
        // 获取具体的枚举实体对象，而不是使用注解
        io.github.alfonsokevin.core.limiter.model.FrequencyControl control =
                io.github.alfonsokevin.core.limiter.model.FrequencyControl.of(frequencyControl);
        // 2.如果容器中没启动redissonClient则直接返回
        if (Objects.isNull(redissonClient)) {
            log.debug("[{FrequencyControl}]: >> 容器中不存在redissonClient，Unable to activate the FrequencyControl..");
            throw FrequencyControlBuilder.build(control.getExceptionClass(), control.getMessage());
        }
        // 3.如果请求的不是http请求，抛出异常
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            log.error("[{FrequencyControl}]: >> Request non HTTP request!! " +
                    "The FrequencyControl will not be able to be used");
            throw FrequencyControlBuilder.build(control.getExceptionClass(), control.getMessage());
        }
        // 速率限流器开始工作....
        log.debug("[{FrequencyControl}]: >> The FrequencyControl starts working");
        HttpServletRequest request = servletRequestAttributes.getRequest();

        // 4.获取速率限流器，如果速率限流器次数达到了要求，那么发出告警
        // 获取具体生成key的策略
        GeneratorKeyStrategy keyStrategy = keyStrategyFactory.getKeyStrategy(control.getKeyType());
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String key = keyStrategy.getKey(control, joinPoint, method, request);

        // 获取工厂，取到自己想要的限流实现
        FrequencyControlStrategy rateLimiterStrategy = limiterTypeStrategyFactory
                .getFrequencyControlStrategy(control.getType());
        boolean result = rateLimiterStrategy.tryAcquire(key, control);
        log.debug("[{FrequencyControl}]: >> Start limiting current key:{}, " +
                        "intervalTimes:{}, unit:{}, rate:{}, url:{}, key:{}",
                control.getKey(), control.getIntervalTimes(), control.getUnit(),
                control.getRate(), request.getRequestURI(), key);
        if (!result) {
            // 直接使用静态缓存构造器避免多次反射
            throw FrequencyControlBuilder.build(control.getExceptionClass(), control.getMessage());
        }
        return joinPoint.proceed();
    }

}
