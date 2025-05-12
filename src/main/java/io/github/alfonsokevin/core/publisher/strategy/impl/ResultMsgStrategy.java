package io.github.alfonsokevin.core.publisher.strategy.impl;

import com.alibaba.fastjson2.JSON;
import io.github.alfonsokevin.core.publisher.model.RedisPublisher;
import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import io.github.alfonsokevin.core.publisher.strategy.PublisherMsgStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description 消息内容-方法返回体
 * @since 2025-05-07 18:48
 * @author TangZhiKai
 **/
@Component
public class ResultMsgStrategy implements PublisherMsgStrategy {

    @Override
    public String getMsg(RedisPublisher redisPublisher, ProceedingJoinPoint joinPoint, Method method, Object result) {
        return JSON.toJSONString(result);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.RESULT;
    }
}
