package io.github.alfonsokevin.core.cache.annotation;

import io.github.alfonsokevin.core.cache.model.enums.EvictKeyType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author TangZhikai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheEvict {
    /**
     * key要删除的键，必填
     */
    String key();

    /**
     * 延迟多少时间进行二次删除
     */
    long delay() default 10L;

    /**
     * 延迟删除的时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 生成key的类型
     */
    EvictKeyType type() default EvictKeyType.DEFAULT;
}
