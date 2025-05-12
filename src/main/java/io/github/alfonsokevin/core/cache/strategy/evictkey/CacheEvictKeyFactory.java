package io.github.alfonsokevin.core.cache.strategy.evictkey;

import io.github.alfonsokevin.core.cache.model.enums.EvictKeyType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description 工厂，获取具体策略，生成key
 * @since 2025-04-27 17:24
 * @author TangZhiKai
 **/
@Component
public class CacheEvictKeyFactory {


    private final Map<EvictKeyType, EvictKeyStrategy> evictStrategyGroup;

    public CacheEvictKeyFactory(List<EvictKeyStrategy> evictKeyStrategyList) {
        evictStrategyGroup = evictKeyStrategyList.stream()
                .collect(Collectors.toMap(EvictKeyStrategy::getKeyType, Function.identity()));
    }

    /**
     * 获取删除key的策略
     * @param eKeyType
     * @return
     */
    public EvictKeyStrategy getEvictKeyStrategy(EvictKeyType eKeyType) {
        return evictStrategyGroup.get(eKeyType);
    }
}
