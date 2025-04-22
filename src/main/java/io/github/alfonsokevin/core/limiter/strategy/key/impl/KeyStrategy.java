package io.github.alfonsokevin.core.limiter.strategy.key.impl;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: 自定义Key的生成策略
 * @create: 2025-04-22 14:54
 * @author: TangZhiKai
 **/
@Component(value = "KEY")
public class KeyStrategy implements GeneratorKeyStrategy {

    /**
     * 直接使用默认的用户自定义的
     * @param frequencyControl
     * @param method
     * @return
     */
    @Override
    public String getKey(FrequencyControl frequencyControl, ProceedingJoinPoint joinPoint, Method method) {
        return frequencyControl.key();
    }
}
