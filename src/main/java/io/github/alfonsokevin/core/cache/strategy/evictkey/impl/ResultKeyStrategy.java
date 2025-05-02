package io.github.alfonsokevin.core.cache.strategy.evictkey.impl;

import io.github.alfonsokevin.core.cache.model.RedisCacheEvict;
import io.github.alfonsokevin.core.cache.model.enums.EvictKeyType;
import io.github.alfonsokevin.core.cache.strategy.evictkey.EvictKeyStrategy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @description: 对方法执行结果缓存的清空
 * @create: 2025-05-02 23:49
 * @author: TangZhiKai
 **/
@Component
public class ResultKeyStrategy implements EvictKeyStrategy {
    @Override
    public String getEvictKey(RedisCacheEvict redisCacheEvict, Method method, Object[] args, Object result) {
        // 获取方法的结果，然后返回String类型
        return Optional.ofNullable(result)
                .map(String::valueOf)
                .orElse(redisCacheEvict.getKey());
    }

    @Override
    public EvictKeyType getKeyType() {
        return EvictKeyType.RESULT;
    }
}
