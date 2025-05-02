package io.github.alfonsokevin.core.cache.aspect;

import io.github.alfonsokevin.core.base.constants.RTConstants;
import io.github.alfonsokevin.core.cache.annotation.RedisCacheEvict;
import io.github.alfonsokevin.core.cache.strategy.evictkey.CacheEvictKeyFactory;
import io.github.alfonsokevin.core.cache.strategy.evictkey.EvictKeyStrategy;
import io.github.alfonsokevin.core.config.RedisToolsAutoConfiguration;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @description: 延迟双删-切入点方法
 * @create: 2025-04-27 14:41
 * @author: TangZhiKai
 **/
@Order(3)
@Aspect
@Component
@AutoConfigureAfter(RedisToolsAutoConfiguration.class)
@RequiredArgsConstructor
public class RedisCacheEvictAspect {

    private final static Logger log = LoggerFactory.getLogger(RedisCacheEvictAspect.class);
    private final RedissonClient redissonClient;
    private final CacheEvictKeyFactory factory;

    @PostConstruct
    public void init() {
        if (Objects.isNull(redissonClient)) {
            log.warn("[{RedisCacheEvict}]: >> redissonClient is init failed ~~");
        }
        log.info("[{RedisCacheEvict}]: >> redissonClient is opening ~~");
    }

    @Pointcut("@annotation(redisCacheEvict)")
    public void pointEvict(RedisCacheEvict redisCacheEvict) {

    }

    @Around("pointEvict(redisCacheEvict)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCacheEvict redisCacheEvict) throws Throwable {
        if (Objects.isNull(redisCacheEvict)) {
            throw new IllegalArgumentException("[{RedisCacheEvict}]: >> redisCacheEvict is null ~~");
        }
        io.github.alfonsokevin.core.cache.model.RedisCacheEvict cacheEvict =
                io.github.alfonsokevin.core.cache.model.RedisCacheEvict.of(redisCacheEvict);
        // TODO 是否需要使用事务，用来保证方法执行结束后操作的原子性
        // 1.执行方法
        Object result = joinPoint.proceed();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        // 2.策略解析key
        EvictKeyStrategy keyStrategy = factory.getEvictKeyStrategy(cacheEvict.getType());
        String currentKey = keyStrategy.getEvictKey(cacheEvict, method, args, result);
        // 3.删除key
        RKeys keys = redissonClient.getKeys();
        if (Objects.isNull(cacheEvict.getKey())) {
            log.warn("[{RedisCacheEvict}]: >> CacheEvict key is null.");
            throw new IllegalArgumentException("CacheEvict key is null.");
        }
        // 0表示不存在，1表示删除成功
        long isDelete = keys.delete(currentKey);
        if (isDelete == 1) {
            // 删除成功
            // 4.加入到Redisson延迟队列中，设置延迟时间
            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(RTConstants.REDIS_CACHE_EVICT_QUEUE);
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            delayedQueue.offer(currentKey, cacheEvict.getDelay(), cacheEvict.getUnit());
        }
        // 5.延迟删除key


        return result;
    }

    @PreDestroy
    public void destory() {
        if (Objects.isNull(redissonClient) || redissonClient.isShutdown()) {
            return;
        }
        redissonClient.shutdown();
        log.info("[{RedisCacheEvict}]: >> redissonClient is destoryed ~~");

    }
}
