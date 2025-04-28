package io.github.alfonsokevin.core.base.exception.impl;


import io.github.alfonsokevin.core.base.exception.AbstractRedisToolsException;
import io.github.alfonsokevin.core.base.exception.code.ResultCode;

/**
 * @description: 缓存的异常类
 * @create: 2025-04-28 12:00
 * @author: TangZhiKai
 **/
public class CacheException extends AbstractRedisToolsException {

    public CacheException(int code) {
        super(code);
    }

    public CacheException(String message, int code) {
        super(message, code);
    }

    public CacheException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public CacheException(Throwable cause, int code) {
        super(cause, code);
    }

    public CacheException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }

    public CacheException(ResultCode resultCode) {
        super(resultCode);
    }

    public CacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
