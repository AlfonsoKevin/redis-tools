package io.github.alfonsokevin.core.limiter.annotation;

import io.github.alfonsokevin.core.limiter.enums.ControlType;
import io.github.alfonsokevin.core.limiter.enums.KeyType;
import io.github.alfonsokevin.core.limiter.exception.FrequencyControlException;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author TangZhikai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FrequencyControl {
    /**
     * http接口请求路径唯一key
     */
    String key() default "";

    /**
     * 限定时间内
     */
    long intervalTimes() default 1L;

    /**
     * 总次数
     */
    long rate() default 1L;

    /**
     * 限流时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 默认的异常枚举
     */
    Class<? extends RuntimeException> exceptionClass() default FrequencyControlException.class;

    /**
     * 默认的抛出异常之后的消息
     */
    String message() default "[{FrequencyControl}]: >> Too many requests, please try again later.";


    /**
     * 限流策略
     */
    ControlType type() default ControlType.DEFAULT;


    /**
     * 使用令牌桶算法的容量
     */

    int capacity() default 100;

    /**
     * 注解的单位
     * @return
     */
    KeyType keyType() default KeyType.KEY;


}
