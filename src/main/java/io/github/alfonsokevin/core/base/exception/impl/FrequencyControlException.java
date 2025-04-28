package io.github.alfonsokevin.core.base.exception.impl;

import io.github.alfonsokevin.core.base.exception.AbstractRedisToolsException;
import io.github.alfonsokevin.core.base.exception.code.ResultCode;

/**
 * @description: 默认的异常类型
 * @create: 2025-04-22 01:35
 * @author: TangZhiKai
 **/
public class FrequencyControlException extends AbstractRedisToolsException {

    public FrequencyControlException(int code) {
        super(code);
    }

    public FrequencyControlException(String message, int code) {
        super(message, code);
    }

    public FrequencyControlException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public FrequencyControlException(Throwable cause, int code) {
        super(cause, code);
    }

    public FrequencyControlException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }

    public FrequencyControlException(ResultCode resultCode) {
        super(resultCode);
    }

    public FrequencyControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }


    public FrequencyControlException(String message) {
        super(message);
    }



}
