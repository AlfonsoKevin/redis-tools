package io.github.alfonsokevin.core.publisher.strategy.impl;

import io.github.alfonsokevin.core.base.utils.SpElUtils;
import io.github.alfonsokevin.core.publisher.model.RedisPublisher;
import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import io.github.alfonsokevin.core.publisher.strategy.PublisherMsgStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description 消息内容-用户自定义内容
 * @since 2025-05-07 18:48
 * @author TangZhiKai
 **/
@Component
public class CustomMsgStrategy implements PublisherMsgStrategy {

    /**
     * 用户自定义内容，通过EL表达式解析，返回解析后的字符串
     *
     * @param redisPublisher redisPublisher
     * @param joinPoint      joinPoint
     * @param method         method
     * @param result         方法的执行结果
     * @return
     */
    @Override
    public String getMsg(RedisPublisher redisPublisher,
                         ProceedingJoinPoint joinPoint,
                         Method method,
                         Object result) {
        String waitMsg = redisPublisher.getMessage();
        // 如果包含，SpringEL表达式，如果不包含，那么就正常返回消息内容
        return waitMsg.contains("#") ?
                String.format("%s#%s", SpElUtils.getMethodPrefix(method),
                        SpElUtils.parseEl(method, joinPoint.getArgs(), waitMsg))
                : redisPublisher.getMessage();

    }

    @Override
    public MsgType getMsgType() {
        return MsgType.CUSTOM;
    }
}
