package io.github.alfonsokevin.core.limiter.strategy.key.factory;


import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import io.github.alfonsokevin.core.limiter.strategy.key.impl.KeyStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: Key的策略工厂
 * @create: 2025-04-22 15:19
 * @author: TangZhiKai
 **/
@Component
public class KeyStrategyFactory {

    private final Map<String, GeneratorKeyStrategy> generatorKeyGroup;

    public KeyStrategyFactory(Map<String,GeneratorKeyStrategy> generatorKeyGroup){
        this.generatorKeyGroup = generatorKeyGroup;
    }

    /**
     * 获取具体的策略
     * @param type
     * @return
     */
    public GeneratorKeyStrategy getKeyStrategy(FrequencyControl.KeyType type){
        return generatorKeyGroup.getOrDefault(type.getName(),new KeyStrategy());
    }


}
