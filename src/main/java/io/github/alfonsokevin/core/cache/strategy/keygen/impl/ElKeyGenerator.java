package io.github.alfonsokevin.core.cache.strategy.keygen.impl;


import io.github.alfonsokevin.core.cache.model.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.KeyType;
import io.github.alfonsokevin.core.cache.strategy.keygen.CacheableKeyGenerator;
import io.github.alfonsokevin.core.base.utils.SpElUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: 使用SpringEL表达式解析
 * @create: 2025-04-23 18:02
 * @author: TangZhiKai
 **/

@Component
public class ElKeyGenerator implements CacheableKeyGenerator {

    /**
     * 生成Key = 前缀key + EL解析key
     * @param cacheable
     * @param joinPoint
     * @param method
     * @return 生成Key = 前缀key + EL解析key
     */
    @Override
    public String getKey(RedisCacheable cacheable, ProceedingJoinPoint joinPoint, Method method) {
        String prefixKey = cacheable.getAutoPrefixKey() ? SpElUtils.getMethodPrefix(method) : "";
        String parseKey = SpElUtils.parseEl(method, joinPoint.getArgs(), cacheable.getKey());
        return String.format("%s:%s", prefixKey, parseKey);
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.EL;
    }
}
