package io.github.alfonsokevin.core.limiter.strategy.key.factory;


import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.enums.KeyType;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import io.github.alfonsokevin.core.limiter.strategy.key.impl.KeyStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description Key的策略工厂
 * @since 2025-04-22 15:19
 * @author TangZhiKai
 **/
@Component
public class KeyStrategyFactory {

    private final Map<KeyType, GeneratorKeyStrategy> generatorKeyGroup;

    public KeyStrategyFactory(List<GeneratorKeyStrategy> keyStrategyList){
        this.generatorKeyGroup = keyStrategyList.stream()
                .collect(Collectors.toMap(GeneratorKeyStrategy::getKeyType, Function.identity()));
    }

    /**
     * 获取具体的策略
     * @param type
     * @return
     */
    public GeneratorKeyStrategy getKeyStrategy(KeyType type){
        return generatorKeyGroup.get(type);
    }


}
