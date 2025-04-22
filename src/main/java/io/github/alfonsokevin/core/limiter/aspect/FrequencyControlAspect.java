package io.github.alfonsokevin.core.limiter.aspect;

import io.github.alfonsokevin.core.config.RedisToolsAutoConfiguration;
import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.exception.FrequencyControlBuilder;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import io.github.alfonsokevin.core.limiter.strategy.key.factory.KeyStrategyFactory;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import io.github.alfonsokevin.core.limiter.strategy.limit.factory.FrequencyControlStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@Slf4j
// 指定在其他自动配置类应用之后启动
@AutoConfigureAfter(RedisToolsAutoConfiguration.class)
@RequiredArgsConstructor
public class FrequencyControlAspect {


    @Autowired
    @Qualifier("redissonToolsClient")
    private RedissonClient redissonClient;


    private final FrequencyControlStrategyFactory limiterTypeStrategyFactory;
    private final KeyStrategyFactory keyStrategyFactory;

    @PostConstruct
    public void init() {
        if (Objects.isNull(redissonClient)) {
            log.error("启动失败，请检查Redis配置");
        }
        log.info("项目启动成功，速率限流器将开启~~ redissonClient:{}",redissonClient.getConfig());
    }

    @Pointcut("@annotation(frequencyControl)")
    public void pointcutLimit(FrequencyControl frequencyControl){

    }

    @Around("pointcutLimit(frequencyControl)")
    public Object around(ProceedingJoinPoint joinPoint, FrequencyControl frequencyControl) throws Throwable {
        // 1.如果不存在rateLimiter直接返回
        if (Objects.isNull(frequencyControl)) {
            log.error("方法上没有添加注解");
            throw new IllegalArgumentException("速率限流器将无法使用");
        }
        // 2.如果容器中没启动redissonClient则直接返回
        if (Objects.isNull(redissonClient)) {
            log.info("容器中不存在redissonClient，无法启动速率限流器..");
            throw FrequencyControlBuilder.build(frequencyControl.exceptionClass(), frequencyControl.message());
        }
        // 3.如果请求的不是http请求，抛出异常
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletRequestAttributes)) {
            log.error("请求非HTTP请求!!速率限流器将无法使用");
            throw FrequencyControlBuilder.build(frequencyControl.exceptionClass(), frequencyControl.message());
        }
        log.info("速率限流器开始工作....");
        HttpServletRequest request = servletRequestAttributes.getRequest();

        // 4.获取速率限流器，如果速率限流器次数达到了要求，那么发出告警
        //获取具体生成key的策略
        GeneratorKeyStrategy keyStrategy = keyStrategyFactory.getKeyStrategy(frequencyControl.keyType());
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String key = keyStrategy.getKey(frequencyControl, joinPoint, method);

        //获取工厂，取到自己想要的限流实现
        FrequencyControlStrategy rateLimiterStrategy = limiterTypeStrategyFactory.getFrequencyControlStrategy(frequencyControl.type());
        boolean result = rateLimiterStrategy.tryAcquire(key, frequencyControl);
        log.info("开始限流 key:{}, intervalTimes:{}, unit:{}, rate:{}, url:{}, key:{}",
                frequencyControl.key(),frequencyControl.intervalTimes(),frequencyControl.unit(),frequencyControl.rate(),request.getRequestURI(),key);
        if (!result) {
            //直接使用静态缓存构造器避免多次反射
            throw FrequencyControlBuilder.build(frequencyControl.exceptionClass(), frequencyControl.message());
        }
        return joinPoint.proceed();
    }

}
