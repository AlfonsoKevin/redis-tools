package io.github.alfonsokevin.core.cache.strategy.nullvalue;

import io.github.alfonsokevin.core.cache.model.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.NullType;

/**
 * @description 空值处理类
 * @since 2025-04-23 19:21
 * @author TangZhiKai
 **/
public interface NullValueHandler {

    /**
     * 空值处理策略
     * @param cacheable
     */
    void handleNullValue(RedisCacheable cacheable, String key);

    NullType getNullHandlerType();
}
