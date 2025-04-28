package io.github.alfonsokevin.core.cache.model;


import io.github.alfonsokevin.core.cache.model.enums.EvictKeyType;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @description: 注解对应实体类
 * @create: 2025-04-27 14:57
 * @author: TangZhiKai
 **/
@Data
public class RedisCacheEvict {
    /**
     * key要删除的键，必填
     */
    private String key;
    /**
     * 延迟多少时间进行二次删除
     */
    private long delay;
    /**
     * 延迟删除的时间单位
     */
    private TimeUnit unit;
    /**
     * 生成key的类型
     */
    private EvictKeyType type;

    public static RedisCacheEvict of(io.github.alfonsokevin.core.cache.annotation.RedisCacheEvict evict) {
        RedisCacheEvict redisCacheEvict = new RedisCacheEvict();
        redisCacheEvict.setKey(evict.key());
        redisCacheEvict.setDelay(evict.delay());
        redisCacheEvict.setUnit(evict.unit());
        redisCacheEvict.setType(evict.type());
        return redisCacheEvict;
    }
}
