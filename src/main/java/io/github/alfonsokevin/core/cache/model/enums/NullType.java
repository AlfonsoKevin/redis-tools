package io.github.alfonsokevin.core.cache.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 缓存为空的时候的对应策略
 * @create: 2025-04-24 16:20
 * @author: TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum NullType {

    /**
     * BLOOM_FILTER 开启布隆过滤器，
     * SET_NULL 缓存空数据
     * NOT 如果缓存为空不做任何操作
     */
    SET_NULL,
    BLOOM_FILTER,
    DEFAULT;

}
