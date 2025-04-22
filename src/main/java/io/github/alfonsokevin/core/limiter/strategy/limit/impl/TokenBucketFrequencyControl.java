package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 令牌桶限流策略
 * @create: 2025-04-22 01:13
 * @author: TangZhiKai
 **/
@Slf4j
@Component("TOKEN_BUCKET")
@RequiredArgsConstructor
public class TokenBucketFrequencyControl implements FrequencyControlStrategy {

    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) {
        return false;
    }
}
