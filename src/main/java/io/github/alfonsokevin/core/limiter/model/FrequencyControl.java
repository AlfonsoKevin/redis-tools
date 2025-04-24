package io.github.alfonsokevin.core.limiter.model;

import io.github.alfonsokevin.core.limiter.enums.ControlType;
import io.github.alfonsokevin.core.limiter.enums.KeyType;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @description: 频率控制信息类
 * @create: 2025-04-24 23:34
 * @author: TangZhiKai
 **/
@Data
public class FrequencyControl {
    private String key;
    private long intervalTimes;
    private long rate;
    private TimeUnit unit;
    private Class<? extends RuntimeException> exceptionClass;
    private String message;
    private ControlType type;
    private int capacity;
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
