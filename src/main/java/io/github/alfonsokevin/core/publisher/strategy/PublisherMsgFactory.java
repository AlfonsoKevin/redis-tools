package io.github.alfonsokevin.core.publisher.strategy;

import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description 消息内容的生成工厂
 * @since 2025-05-07 18:49
 * @author TangZhiKai
 **/
@Component
public class PublisherMsgFactory {
    private final Map<MsgType, PublisherMsgStrategy> publisherMsgGroup;

    public PublisherMsgFactory(List<PublisherMsgStrategy> publisherMsgStrategyList) {
        publisherMsgGroup = publisherMsgStrategyList
                .stream()
                .collect(Collectors.toMap(PublisherMsgStrategy::getMsgType,
                        Function.identity()));
    }

    /**
     * 获取具体的内容生成策略
     * @param type
     * @return
     */
    public PublisherMsgStrategy getMsgStrategy(MsgType type) {
        return publisherMsgGroup.get(type);
    }

}
