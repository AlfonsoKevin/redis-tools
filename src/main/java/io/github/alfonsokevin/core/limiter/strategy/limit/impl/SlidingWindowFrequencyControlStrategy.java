package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @description: 滑动窗口策略限流
 * @create: 2025-04-22 01:21
 * @author: TangZhiKai
 **/
@Component(value = "REDIS_FRE_SLIDING_WINDOW")
@RequiredArgsConstructor //将所有final字段构造函数中初始化
public class SlidingWindowFrequencyControlStrategy implements FrequencyControlStrategy {

    private static final Logger log = LoggerFactory.getLogger(SlidingWindowFrequencyControlStrategy.class);

    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) {
        return false;
    }
}
