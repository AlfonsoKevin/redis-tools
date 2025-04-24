package io.github.alfonsokevin.core.limiter.strategy.key;

import io.github.alfonsokevin.core.limiter.model.FrequencyControl;
import org.aspectj.lang.ProceedingJoinPoint;
import java.lang.reflect.Method;

/**
 * @description: 生成RedisKey的策略
 * @create: 2025-04-22 14:43
 * @author: TangZhiKai
 **/
public interface GeneratorKeyStrategy {

    /**
     * 根据不同策略获取key
     * 这三个参数主要是适配EL表达式的
     * @param frequencyControl
     * @param joinPoint
     * @param method
     * @return 不同策略下的key
     */
    String getKey(FrequencyControl frequencyControl, ProceedingJoinPoint joinPoint, Method method);
}
