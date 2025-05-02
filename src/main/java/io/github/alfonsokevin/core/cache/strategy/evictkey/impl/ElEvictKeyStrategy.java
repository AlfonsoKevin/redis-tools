package io.github.alfonsokevin.core.cache.strategy.evictkey.impl;

import io.github.alfonsokevin.core.base.utils.SpElUtils;
import io.github.alfonsokevin.core.cache.model.RedisCacheEvict;
import io.github.alfonsokevin.core.cache.model.enums.EvictKeyType;
import io.github.alfonsokevin.core.cache.strategy.evictkey.EvictKeyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: EL表达式解析Key
 * @create: 2025-04-27 17:21
 * @author: TangZhiKai
 **/
@Component
public class ElEvictKeyStrategy implements EvictKeyStrategy {
    private final static Logger log = LoggerFactory.getLogger(ElEvictKeyStrategy.class);

    /**
     * 获取要删除的key
     * 使用SpringEL表达式
     *
     * @param redisCacheEvict 注解实体
     * @return target key
     */
    @Override
    public String getEvictKey(RedisCacheEvict redisCacheEvict,
                              Method method, Object[] args, Object result) {
        log.info("[{RedisCacheEvict}]: >> Key strategy: {}", this.getKeyType());
        String key = redisCacheEvict.getKey();

        return SpElUtils.parseEl(method, args, key);
    }

    /**
     * key的策略
     *
     * @return
     */
    @Override
    public EvictKeyType getKeyType() {
        return EvictKeyType.EL;
    }
}
