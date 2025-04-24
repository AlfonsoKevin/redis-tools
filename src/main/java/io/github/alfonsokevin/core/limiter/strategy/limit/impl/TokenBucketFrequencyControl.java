package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import io.github.alfonsokevin.core.limiter.model.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

/**
 * @description: 令牌桶限流策略
 * @create: 2025-04-22 01:13
 * @author: TangZhiKai
 **/
@Component("REDIS_FREQ_TOKEN_BUCKET")
@RequiredArgsConstructor
public class TokenBucketFrequencyControl implements FrequencyControlStrategy {
    private static final Logger log = LoggerFactory.getLogger(TokenBucketFrequencyControl.class);
    private final StringRedisTemplate stringRedisTemplate;
    private final ResourceLoader resourceLoader;

    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) {
        return false;
    }

    @Override
    public String loadScript(String path) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
