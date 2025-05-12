package io.github.alfonsokevin.core.publisher.strategy;

import io.github.alfonsokevin.core.publisher.model.RedisPublisher;
import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @description 发布topic对于消息策略
 * @since 2025-05-07 18:46
 * @author TangZhiKai
 **/
public interface PublisherMsgStrategy {

    /**
     * 获取消息
     * @param redisPublisher redisPublisher
     * @param joinPoint joinPoint
     * @param method method
     * @param result 方法的执行结果
     * @return
     */
    String getMsg(RedisPublisher redisPublisher, ProceedingJoinPoint joinPoint, Method method, Object result);

    /**
     * 获取消息策略类型
     * @return
     */
    MsgType getMsgType();
}
