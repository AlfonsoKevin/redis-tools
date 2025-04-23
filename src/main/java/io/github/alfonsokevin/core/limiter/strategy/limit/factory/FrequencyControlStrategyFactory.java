package io.github.alfonsokevin.core.limiter.strategy.limit.factory;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 策略工厂
 * @author: TangZhiKai
 **/
@Component
public class FrequencyControlStrategyFactory {
    private final Map<String, FrequencyControlStrategy> limitStrategyGroup;
    //构造注入
    public FrequencyControlStrategyFactory(Map<String, FrequencyControlStrategy> limitStrategyGroup) {
        this.limitStrategyGroup = limitStrategyGroup;
    }

    public FrequencyControlStrategy getFrequencyControlStrategy(FrequencyControl.ControlType type){
        return limitStrategyGroup.get(type.toString());
    }

}
