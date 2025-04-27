package io.github.alfonsokevin.core.cache.annotation;

import io.github.alfonsokevin.core.cache.model.enums.KeyType;
import io.github.alfonsokevin.core.cache.model.enums.NullType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author TangZhikai
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface RedisCacheable {
    /**
     * key，如果是默认的就是所有的入参拼接
     * EL 就是EL表达式的解析
     *
     * @return
     */
    String key() default "";

    /**
     * 过期时间
     *
     * @return
     */
    long expireTime() default 5 * 60 * 1000L;

    /**
     * 缓存时间
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    /**
     * 缓存key的生成策略
     *
     * @return
     */
    KeyType keyType() default KeyType.DEFAULT;

    /**
     * 缓存为空的时候的策略
     *
     * @return
     */
    NullType nullType() default NullType.SET_NULL;


    /**
     * 防止缓存雪崩，可以原有基础上给定随机值
     * 时间单位需要和unit保持一致
     * @return
     */
    long randomTime() default 0L;

    /**
     * 是否需要拼接前缀名
     * 方法的完全限定名  默认为true需要
     *
     * @return
     */
    boolean autoPrefixKey() default true;

}
