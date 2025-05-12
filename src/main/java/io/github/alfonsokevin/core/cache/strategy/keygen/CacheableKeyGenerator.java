package io.github.alfonsokevin.core.cache.strategy.keygen;

import io.github.alfonsokevin.core.cache.model.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.KeyType;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @description 生成key的策略
 * @since 2025-04-23 18:01
 * @author TangZhiKai
 **/
public interface CacheableKeyGenerator {

    /**
     * 不同的生成key的策略
     *
     * @param cacheable
     * @param joinPoint
     * @param method
     * @return
     */
    String getKey(RedisCacheable cacheable, ProceedingJoinPoint joinPoint, Method method);

    /**
     * 获取对应的枚举类型
     *
     * @return
     */
    KeyType getKeyType();
}
