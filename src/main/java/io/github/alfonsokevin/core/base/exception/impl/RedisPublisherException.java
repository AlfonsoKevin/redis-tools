package io.github.alfonsokevin.core.base.exception.impl;


import io.github.alfonsokevin.core.base.exception.AbstractRedisToolsException;
import io.github.alfonsokevin.core.base.exception.code.ResultCode;

/**
 * @author TangZhiKai
 * @description 发布者异常
 * @since 2025/5/12
 **/
public class RedisPublisherException extends AbstractRedisToolsException {

    public RedisPublisherException(int code) {
        super(code);
    }

    public RedisPublisherException(String message, int code) {
        super(message, code);
    }

    public RedisPublisherException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public RedisPublisherException(Throwable cause, int code) {
        super(cause, code);
    }

    public RedisPublisherException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }

    public RedisPublisherException(ResultCode resultCode) {
        super(resultCode);
    }

    public RedisPublisherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
