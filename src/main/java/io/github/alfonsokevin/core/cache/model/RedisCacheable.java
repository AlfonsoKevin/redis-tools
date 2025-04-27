package io.github.alfonsokevin.core.cache.model;

import io.github.alfonsokevin.core.cache.model.enums.KeyType;
import io.github.alfonsokevin.core.cache.model.enums.NullType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @description: Cacheable注解信息实体类
 * @create: 2025-04-23 17:26
 * @author: TangZhiKai
 **/
@Data
@Getter
@Setter
public class RedisCacheable {
    /**
     * key 如果是默认的就是所有的入参拼接
     */
    private String key;
    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * 缓存时间
     */
    private TimeUnit unit;
    /**
     * 缓存key的生成策略
     */
    private KeyType keyType;
    /**
     * DEFAULT 默认策略
     */
    private NullType nullType;
    /**
     * 缓存为空的时候的策略
     */
    private Long randomTime;

    /**
     * 是否需要拼接前缀名
     * 方法的完全限定名
     *
     * @return 默认为true 需要
     */
    private Boolean autoPrefixKey;

    /**
     * 转换为具体的实体
     *
     * @param annotation
     * @return
     */
    public static RedisCacheable of(io.github.alfonsokevin.core.cache.annotation.RedisCacheable annotation) {
        RedisCacheable cacheable = new RedisCacheable();
        cacheable.setKey(annotation.key());
        cacheable.setExpireTime(annotation.expireTime());
        cacheable.setUnit(annotation.unit());
        cacheable.setKeyType(annotation.keyType());
        cacheable.setNullType(annotation.nullType());
        cacheable.setRandomTime(annotation.randomTime());
        cacheable.setAutoPrefixKey(annotation.autoPrefixKey());
        return cacheable;
    }
}
