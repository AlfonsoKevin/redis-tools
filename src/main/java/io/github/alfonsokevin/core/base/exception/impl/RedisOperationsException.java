package io.github.alfonsokevin.core.base.exception.impl;

import io.github.alfonsokevin.core.base.exception.AbstractRedisToolsException;
import io.github.alfonsokevin.core.base.exception.code.ResultCode;

/**
 * @author TangZhiKai
 * @description Redis基础工具类的异常
 * @since 2025-05-12 23:10
 **/
public class RedisOperationsException extends AbstractRedisToolsException {
    public RedisOperationsException(int code) {
        super(code);
    }

    public RedisOperationsException(String message) {
        super(message);
    }

    public RedisOperationsException(String message, int code) {
        super(message, code);
    }

    public RedisOperationsException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public RedisOperationsException(Throwable cause, int code) {
        super(cause, code);
    }

    public RedisOperationsException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }

    public RedisOperationsException(ResultCode resultCode) {
        super(resultCode);
    }

    public RedisOperationsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }

    @Override
    public int getCode() {
        return super.getCode();
    }
}
