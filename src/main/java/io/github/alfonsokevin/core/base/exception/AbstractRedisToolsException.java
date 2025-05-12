package io.github.alfonsokevin.core.base.exception;


import io.github.alfonsokevin.core.base.exception.code.ResultCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @description 父类
 * @since 2025-04-28 11:50
 * @author TangZhiKai
 **/
public abstract class AbstractRedisToolsException extends RuntimeException
        implements RedisToolsException {
    protected int code;

    protected AbstractRedisToolsException(int code) {
        this.code = code;
    }

    protected AbstractRedisToolsException(String message) {
        super(message);
    }

    protected AbstractRedisToolsException(String message, int code) {
        super(message);
        this.code = code;
    }

    protected AbstractRedisToolsException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    protected AbstractRedisToolsException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    protected AbstractRedisToolsException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
    }

    protected AbstractRedisToolsException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    protected AbstractRedisToolsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
