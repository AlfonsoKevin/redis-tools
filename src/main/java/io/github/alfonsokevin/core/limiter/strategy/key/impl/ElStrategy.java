package io.github.alfonsokevin.core.limiter.strategy.key.impl;


import io.github.alfonsokevin.core.limiter.model.FrequencyControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import io.github.alfonsokevin.core.utils.SpElUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: SpringEl表达式Key的生成策略
 * @create: 2025-04-22 14:55
 * @author: TangZhiKai
 **/
@Component(value = "REDIS_FRE_EL")
public class ElStrategy implements GeneratorKeyStrategy {

    private static final Logger log = LoggerFactory.getLogger(ElStrategy.class);

    @Override
    public String getKey(FrequencyControl frequencyControl, ProceedingJoinPoint joinPoint, Method method) {
        String originalKey = frequencyControl.getKey();
        if (originalKey == null || originalKey.length() == 0) {
            log.debug("[{FrequencyControl}]: >> params error");
            throw new IllegalArgumentException("参数异常");
        }
        String parseKey = SpElUtils.parseEl(method, joinPoint.getArgs(), originalKey);
        String methodPrefix = SpElUtils.getMethodPrefix(method);
        log.debug("[{FrequencyControl}]: >> keyTypeStrategy:{}", frequencyControl.getKeyType().toString());
        return this.getCacheKey(methodPrefix, parseKey);
    }

    /**
     * 获取解析之后的完整key
     *
     * @param methodPrefix
     * @param parseKey
     * @return
     */
    private String getCacheKey(String methodPrefix, String parseKey) {
        return String.format("%s:%s", methodPrefix, parseKey);
    }
}

