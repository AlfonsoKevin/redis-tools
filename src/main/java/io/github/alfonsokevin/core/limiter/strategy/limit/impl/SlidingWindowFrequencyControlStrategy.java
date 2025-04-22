package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 滑动窗口策略限流
 * @create: 2025-04-22 01:21
 * @author: TangZhiKai
 **/
@Slf4j
@Component(value = "SLIDING_WINDOW")
@RequiredArgsConstructor //将所有final字段构造函数中初始化
public class SlidingWindowFrequencyControlStrategy implements FrequencyControlStrategy {

    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) {
        return false;
    }
}
