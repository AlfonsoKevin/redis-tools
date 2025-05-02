package io.github.alfonsokevin.core.cache.strategy.evictkey;

import io.github.alfonsokevin.core.cache.model.RedisCacheEvict;
import io.github.alfonsokevin.core.cache.model.enums.EvictKeyType;

import java.lang.reflect.Method;

/**
 * @description: 删除key的策略
 * @create: 2025-04-27 17:18
 * @author: TangZhiKai
 **/
public interface EvictKeyStrategy {


    /**
     * 获取要删除的key
     *
     * @param redisCacheEvict
     * @return target key
     */
    String getEvictKey(RedisCacheEvict redisCacheEvict, Method method, Object[] args, Object result);

    /**
     * key的策略
     *
     * @return
     */
    EvictKeyType getKeyType();
}
