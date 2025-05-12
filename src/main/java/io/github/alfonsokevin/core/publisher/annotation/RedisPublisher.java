package io.github.alfonsokevin.core.publisher.annotation;

import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Order(4)
public @interface RedisPublisher {

    /**
     * 消息的主题topic
     * @return
     */
    String topic();

    /**
     * 消息的内容，通过发送消息的类型进行区分
     * @see this#type()
     * @return
     */
    String message() default "message";

    /**
     * 发送消息的类型
     * @return
     */
    MsgType type() default MsgType.RESULT;

}
