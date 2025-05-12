package io.github.alfonsokevin.core.publisher.strategy.impl;

import io.github.alfonsokevin.core.base.exception.code.PublisherResultCode;
import io.github.alfonsokevin.core.base.exception.code.StandardResultCode;
import io.github.alfonsokevin.core.base.exception.impl.RedisPublisherException;
import io.github.alfonsokevin.core.base.utils.SpElUtils;
import io.github.alfonsokevin.core.publisher.model.RedisPublisher;
import io.github.alfonsokevin.core.publisher.model.enums.MsgType;
import io.github.alfonsokevin.core.publisher.strategy.PublisherMsgStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description 消息内容-方法参数
 * @since 2025-05-07 18:47
 * @author TangZhiKai
 **/
@Component
public class ParamsMsgStrategy implements PublisherMsgStrategy {

    /**
     * 方法参数
     * 方法有多个参数，用户输入msg指定具体使用的参数
     *
     * @param redisPublisher redisPublisher
     * @param joinPoint      joinPoint
     * @param method         method
     * @param result         方法的执行结果
     * @return 解析好的信息
     */
    @Override
    public String getMsg(RedisPublisher redisPublisher, ProceedingJoinPoint joinPoint, Method method, Object result) {
        String[] params = SpElUtils.NAME_DISCOVERER.getParameterNames(method);
        // 确定用户使用的具体参数
        String message = redisPublisher.getMessage();
        if (message == null || message.length() == 0 || params == null || params.length == 0 ) {
            throw new RedisPublisherException(StandardResultCode.REQUEST_PARAMETER_IS_NULL);
        }
        for (String param : params) {
            if (message.equals(param)) {
                //找到用户定义的了，那么直接返回
                return param;
            }
        }
        throw new RedisPublisherException(PublisherResultCode.PUBLISHER_SIDE_REQUEST_PARAMETER_OUT_OF_ALLOWED_RANGE);
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.PARAMS;
    }
}
