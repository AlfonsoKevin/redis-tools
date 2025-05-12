package io.github.alfonsokevin.core.cache.strategy.nullvalue.impl;

import io.github.alfonsokevin.core.cache.model.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.NullType;
import io.github.alfonsokevin.core.cache.strategy.nullvalue.NullValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description 默认的对于空值的处理
 * @since 2025-04-24 19:32
 * @author TangZhiKai
 **/
@Component
public class DefaultNullTypeHandler  implements NullValueHandler {
    private final static Logger log = LoggerFactory.getLogger(SetNullHandler.class);
    private final StringRedisTemplate stringRedisTemplate;
    public DefaultNullTypeHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public void handleNullValue(RedisCacheable cacheable, String key) {
        log.info("[{RedisCacheable}]: >> Use Strategy: {}  ",
                cacheable.getNullType().toString());
        return ;
    }

    @Override
    public NullType getNullHandlerType() {
        return NullType.DEFAULT;
    }
}
