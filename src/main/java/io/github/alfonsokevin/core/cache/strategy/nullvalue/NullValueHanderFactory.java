package io.github.alfonsokevin.core.cache.strategy.nullvalue;

import io.github.alfonsokevin.core.cache.model.enums.NullType;
import org.springframework.stereotype.Component;;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 策略工厂
 * @create: 2025-04-23 19:24
 * @author: TangZhiKai
 **/
@Component
public class NullValueHanderFactory {

    private final Map<NullType, NullValueHandler> handlerGroup;

    public NullValueHanderFactory(List<NullValueHandler> handlerList) {
        this.handlerGroup = handlerList.stream()
                .collect(Collectors.toMap(NullValueHandler::getNullHandlerType, Function.identity()));
    }

    public NullValueHandler getNullValueHander(NullType type) {
        return handlerGroup.get(type);
    }
}
