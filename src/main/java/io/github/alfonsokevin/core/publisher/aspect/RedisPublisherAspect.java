package io.github.alfonsokevin.core.publisher.aspect;

import io.github.alfonsokevin.core.base.exception.code.StandardResultCode;
import io.github.alfonsokevin.core.base.exception.impl.RedisPublisherException;
import io.github.alfonsokevin.core.config.RedisToolsAutoConfiguration;
import io.github.alfonsokevin.core.publisher.annotation.RedisPublisher;
import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import io.github.alfonsokevin.core.publisher.strategy.PublisherMsgFactory;
import io.github.alfonsokevin.core.publisher.strategy.PublisherMsgStrategy;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * @description 切入点
 * @since 2025-05-07 18:37
 * @author TangZhiKai
 **/
@Aspect
@Order(4)
@Component
@AutoConfigureAfter(RedisToolsAutoConfiguration.class)
@RequiredArgsConstructor
public class RedisPublisherAspect {
    private final static Logger log = LoggerFactory.getLogger(RedisPublisherAspect.class);
    private final RedissonClient redissonClient;
    private final PublisherMsgFactory msgFactory;

    @PostConstruct
    public void init() {
        if (Objects.isNull(redissonClient)) {
            log.warn("[{RedisPublisher}]: >> redissonClient is init failed ~~");
        }
        log.info("[{RedisPublisher}]: >> RedisPublisher is opening ~~");
    }

    @Pointcut("@annotation(redisPublisher)")
    public void pointPublisher(RedisPublisher redisPublisher) {

    }

    @Around(value = "pointPublisher(redisPublisher)")
    public Object around(ProceedingJoinPoint joinPoint, RedisPublisher redisPublisher) throws Throwable {
        if (Objects.isNull(redisPublisher)) {
            throw new RedisPublisherException("[{RedisPublisher}]: >> RedisPublisher is null ~~",
                    StandardResultCode.REQUEST_PARAMETER_IS_NULL.getCode());
        }
        io.github.alfonsokevin.core.publisher.model.RedisPublisher publisher =
                io.github.alfonsokevin.core.publisher.model.RedisPublisher.of(redisPublisher);
        // 1.执行方法，执行方法结束后获取具体的策略
        Object result = joinPoint.proceed();
        // 2.根据不同的策略生成不同的消息内容
        PublisherMsgStrategy msgStrategy = msgFactory.getMsgStrategy(Optional.ofNullable(redisPublisher.type())
                .orElse(MsgType.RESULT));
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String msg = msgStrategy.getMsg(publisher, joinPoint, method, result);
        // TODO 动态生成内容,用户可以将需要的消息存放到从topic中发送吗
        // 3.发布消息
        RTopic topic = redissonClient.getTopic(publisher.getTopic());
        topic.publish(msg);
        log.info("[{RedisPublisher}]: >> topic: {} , message: {} ", publisher.getTopic(), msg);
        return result;
    }
}
