package io.github.alfonsokevin.core.cache.strategy.evictkey.impl;

import io.github.alfonsokevin.core.cache.model.RedisCacheEvict;
import io.github.alfonsokevin.core.cache.model.enums.EvictKeyType;
import io.github.alfonsokevin.core.cache.strategy.evictkey.EvictKeyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: 默认删除key的策略
 * @create: 2025-04-27 17:18
 * @author: TangZhiKai
 **/
@Component
public class DefaultEvictKeyStrategy implements EvictKeyStrategy {
    private final static Logger log = LoggerFactory.getLogger(DefaultEvictKeyStrategy.class);

    /**
     * 获取要删除的key
     *
     * @param redisCacheEvict
     * @return target key
     */
    @Override
    public String getEvictKey(RedisCacheEvict redisCacheEvict,
                              Method method, Object[] args,Object result) {
        log.info("[{RedisCacheEvict}]: >> Key strategy: {}", this.getKeyType());
        return redisCacheEvict.getKey();
    }

    @Override
    public EvictKeyType getKeyType() {
        return EvictKeyType.DEFAULT;
    }
}
