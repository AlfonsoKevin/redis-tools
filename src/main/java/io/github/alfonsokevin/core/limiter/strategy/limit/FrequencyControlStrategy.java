package io.github.alfonsokevin.core.limiter.strategy.limit;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;

/**
 * @description: 限流策略
 * @create: 2025-04-22 01:13
 * @author: TangZhiKai
 **/
public interface FrequencyControlStrategy {
    /**
     *
     * @param key 解析好的key
     * @param frequencyControl 速率限流器注解
     * @return
     */
    boolean tryAcquire(String key, FrequencyControl frequencyControl);
}
