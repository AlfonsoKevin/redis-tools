package io.github.alfonsokevin.core.cache.aspect;



import com.alibaba.fastjson2.JSON;
import io.github.alfonsokevin.core.cache.annotation.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.KeyType;
import io.github.alfonsokevin.core.cache.model.enums.NullType;
import io.github.alfonsokevin.core.cache.strategy.keygen.CacheableKeyFactory;
import io.github.alfonsokevin.core.cache.strategy.keygen.CacheableKeyGenerator;
import io.github.alfonsokevin.core.cache.strategy.nullvalue.NullValueHanderFactory;
import io.github.alfonsokevin.core.cache.strategy.nullvalue.NullValueHandler;
import io.github.alfonsokevin.core.config.RedisToolsAutoConfiguration;
import org.apache.commons.lang3.RandomUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 增强
 * @create: 2025-04-23 17:23
 * @author: TangZhiKai
 **/
@Order(2)
@Aspect
@Component
@AutoConfigureAfter(RedisToolsAutoConfiguration.class)
public class RedisCacheableAspect {
    private final static Logger logger = LoggerFactory.getLogger(RedisCacheableAspect.class);


    private final StringRedisTemplate template;
    private final CacheableKeyFactory keyFactory;
    private final NullValueHanderFactory nullValueHanderFactory;
    private final StringRedisTemplate redisTemplate;


    public RedisCacheableAspect(StringRedisTemplate template, CacheableKeyFactory keyFactory,
                                NullValueHanderFactory nullValueHanderFactory,
                                StringRedisTemplate redisTemplate) {
        this.template = template;
        this.keyFactory = keyFactory;
        this.nullValueHanderFactory = nullValueHanderFactory;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        if (Objects.isNull(template)) {
            logger.error("[{RedisCacheable}]: >> redissonClient is error ~~~");
        }
        logger.info("[{RedisCacheable}]: >> redissonClient is finishing ~~~~");
    }

    @Pointcut("@annotation(rc)")
    public void pointCacheable(RedisCacheable rc) {

    }

    /**
     * 注解增强
     *
     * @param joinPoint      切入点
     * @param redisCacheable 注解信息
     * @return 如果为空，执行策略并返回null，否则返回值
     * @throws Throwable
     */
    @Around("@annotation(redisCacheable)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCacheable redisCacheable) throws Throwable {
        // 1.判断注解如果没有添加，抛出异常
        if (Objects.isNull(redisCacheable)) {
            throw new IllegalArgumentException("[{RedisCacheable}]: >> redisCacheable is null on method!");
        }
        // 2.转换为具体的实体，进行判断，获取redistemplate
        io.github.alfonsokevin.core.cache.model.RedisCacheable cacheable = io.github.alfonsokevin.core.cache.model.RedisCacheable.of(redisCacheable);
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (Objects.isNull(method)) {
            throw new IllegalArgumentException("[{RedisCacheable}]: >>  getMethod() is failed");
        }
        // 3.获取具体的生成key策略
        String currentKey = getKeyStrategy(joinPoint, cacheable, method);
        // 4.从缓存中查询是否有key，存在直接返回（序列化）
        String queryValue = redisTemplate.opsForValue().get(currentKey);


        // 获取方法的返回体类型
        Class<?> returnType = method.getReturnType();
        if (Objects.nonNull(queryValue)) {
            // 如果查询的结果不为空，提前返回了具体类型的对象
            return JSON.parseObject(queryValue, returnType);
        }
        // 如果为空，就要执行方法了
        Object result = joinPoint.proceed();

        // 5.如果返回为空，获取,执行具体的空值策略，否则获取返回的结果，序列化成存入缓存
        if (Objects.nonNull(result)) {
            String value = JSON.toJSONString(result);
            // 避免缓存雪崩
            long realTime = getRealTime(cacheable);
            // TODO 工具类
            redisTemplate.opsForValue().set(currentKey, value, realTime, cacheable.getUnit());
            return value;
        }
        // 为空那么执行策略类,看看有没有必要执行其他逻辑
        getNullValueSetrategy(cacheable, currentKey);
        // TODO 可能需要调整返回值后期继续使用，拓展
        // 最后只能返回null
        return null;
    }


    /**
     * 获取真实的时间 原有时间 + 范围时间的内的随机时间
     *
     * @param cacheable 注释
     * @return 毫秒数
     */
    private long getRealTime(io.github.alfonsokevin.core.cache.model.RedisCacheable cacheable) {
        if (cacheable.getRandomTime() <= 0L) {
            //如果没有设置，或者是不合法，那么就需要随机时间
            return cacheable.getExpireTime();
        }
        TimeUnit cacheTimeUnit = cacheable.getUnit();
        long originalTime = cacheTimeUnit.toMillis(cacheable.getExpireTime());
        long randomTime = cacheTimeUnit.toMillis(cacheable.getRandomTime());
        long rangeTime = RandomUtils.nextLong(0, randomTime + 1);
        return originalTime + rangeTime;

    }

    /**
     * 得到具体的空值策略，并且执行
     *
     * @param cacheable
     * @param key
     * @return
     */
    private void getNullValueSetrategy(io.github.alfonsokevin.core.cache.model.RedisCacheable cacheable, String key) {
        NullType nullType = cacheable.getNullType();
        NullValueHandler nullValueHander = nullValueHanderFactory.getNullValueHander(nullType);
        nullValueHander.handleNullValue(cacheable, key);
    }

    /**
     * 得到具体的生成key的策略，并执行
     *
     * @param joinPoint
     * @param cacheable
     * @param method
     * @return
     */
    private String getKeyStrategy(ProceedingJoinPoint joinPoint, io.github.alfonsokevin.core.cache.model.RedisCacheable cacheable, Method method) {
        KeyType type = cacheable.getKeyType();
        CacheableKeyGenerator keyStrategy = keyFactory.getGeneratorStrategy(type);
        String key = keyStrategy.getKey(cacheable, joinPoint, method);
        return key;
    }


}
