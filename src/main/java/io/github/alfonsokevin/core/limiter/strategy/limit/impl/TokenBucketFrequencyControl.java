package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.aspect.FrequencyControlAspect;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @description: 令牌桶限流策略
 * @create: 2025-04-22 01:13
 * @author: TangZhiKai
 **/
@Component("REDIS_FRE_TOKEN_BUCKET")
@RequiredArgsConstructor
public class TokenBucketFrequencyControl implements FrequencyControlStrategy {
    private static final Logger log = LoggerFactory.getLogger(TokenBucketFrequencyControl.class);
    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) {
        return false;
    }
}
