package io.github.alfonsokevin.core.publisher.model;

import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @description Redis发布订阅注解实体类
 * @since 2025-05-07 18:33
 * @author TangZhiKai
 **/
@Data
@Getter
@Setter
public class RedisPublisher {
    /**
     * 消息的主题topic
     */
    private String topic;
    /**
     * 消息的内容，通过发送消息的类型进行区分
     */
    private String message;
    /**
     * 发送消息的类型
     */
    private MsgType type;

    public static RedisPublisher of(io.github.alfonsokevin.core.publisher.annotation.RedisPublisher publisher) {
        RedisPublisher redisPublisher = new RedisPublisher();
        redisPublisher.setTopic(publisher.topic());
        redisPublisher.setMessage(publisher.message());
        redisPublisher.setType(publisher.type());
        return redisPublisher;
    }
}
