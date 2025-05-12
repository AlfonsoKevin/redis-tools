package io.github.alfonsokevin.core.limiter.model;

import io.github.alfonsokevin.core.base.exception.AbstractRedisToolsException;
import io.github.alfonsokevin.core.limiter.enums.ControlType;
import io.github.alfonsokevin.core.limiter.enums.KeyType;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @description 频率控制信息类
 * @since 2025-04-24 23:34
 * @author TangZhiKai
 **/
@Data
public class FrequencyControl {
    /**
     * http接口请求路径唯一key
     */
    private String key;
    /**
     * 限定时间内
     */
    private long intervalTimes;
    /**
     * 总次数
     */
    private long rate;
    /**
     * 限流时间单位
     */
    private TimeUnit unit;
    /**
     * 默认的异常枚举
     */
    private Class<? extends AbstractRedisToolsException> exceptionClass;
    /**
     * 默认的抛出异常之后的消息
     */
    private String message;
    /**
     * 限流策略
     */
    private ControlType type;
    /**
     * 使用令牌桶算法的容量
     */
    private int capacity;
    /**
     * 注解的单位
     */
    private KeyType keyType;

    /**
     * 注解转换为枚举
     * @param control
     * @return
     */
    public static FrequencyControl of(
            io.github.alfonsokevin.core.limiter.annotation.FrequencyControl control) {
        FrequencyControl frequencyControl = new FrequencyControl();
        frequencyControl.setKey(control.key());
        frequencyControl.setIntervalTimes(control.intervalTimes());
        frequencyControl.setRate(control.rate());
        frequencyControl.setUnit(control.unit());
        frequencyControl.setExceptionClass(control.exceptionClass());
        frequencyControl.setMessage(control.message());
        frequencyControl.setType(control.type());
        frequencyControl.setCapacity(control.capacity());
        frequencyControl.setKeyType(control.keyType());
        return frequencyControl;
    }

}
