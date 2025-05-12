package io.github.alfonsokevin.core.limiter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 策略限流枚举
 * @since 2025-04-24 23:38
 * @author TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum ControlType {
    /**
     * 默认
     * 令牌桶
     * 滑动窗口
     */
    DEFAULT("REDIS_FREQ_DEFAULT"),
    TOKEN_BUCKET("REDIS_FREQ_TOKEN_BUCKET"),
    SLIDING_WINDOW("REDIS_FREQ_SLIDING_WINDOW");

    private final String name;
}
