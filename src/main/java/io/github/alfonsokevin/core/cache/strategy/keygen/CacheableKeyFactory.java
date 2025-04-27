package io.github.alfonsokevin.core.cache.strategy.keygen;


import io.github.alfonsokevin.core.cache.model.enums.KeyType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: key生成工厂
 * @create: 2025-04-23 19:16
 * @author: TangZhiKai
 **/
@Component
public class CacheableKeyFactory {

    private final Map<KeyType, CacheableKeyGenerator> cacheableKeyGroup;

    public CacheableKeyFactory(List<CacheableKeyGenerator> generatorList) {
        cacheableKeyGroup = generatorList.stream().collect(Collectors.toMap(
                CacheableKeyGenerator::getKeyType,
                Function.identity()));
    }

    /**
     * 生成具体策略
     * @param keyType 生成key的策略枚举
     * @return 对应策略类
     */
    public CacheableKeyGenerator getGeneratorStrategy(KeyType keyType) {
        return cacheableKeyGroup.get(keyType);
    }

}
