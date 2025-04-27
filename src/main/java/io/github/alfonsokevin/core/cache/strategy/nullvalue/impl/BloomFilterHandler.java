package io.github.alfonsokevin.core.cache.strategy.nullvalue.impl;

import io.github.alfonsokevin.core.cache.model.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.NullType;
import io.github.alfonsokevin.core.cache.strategy.nullvalue.NullValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: 布隆过滤器的处理类
 * @create: 2025-04-23 19:24
 * @author: TangZhiKai
 **/
@Component
public class BloomFilterHandler implements NullValueHandler {

    private final static Logger log = LoggerFactory.getLogger(SetNullHandler.class);
    private final StringRedisTemplate stringRedisTemplate;

    public BloomFilterHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    /**
     * 处理空值的策略
     * @param cacheable
     * @param key 解析后的key
     * @return
     */
    @Override
    public void handleNullValue(RedisCacheable cacheable, String key) {
        log.info("[{RedisCacheable}]: >> Use Strategy: {} ",
                cacheable.getNullType().toString());
    }

    @Override
    public NullType getNullHandlerType() {
        return NullType.BLOOM_FILTER;
    }
}
