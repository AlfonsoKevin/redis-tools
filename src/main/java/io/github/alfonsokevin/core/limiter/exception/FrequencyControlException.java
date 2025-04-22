package io.github.alfonsokevin.core.limiter.exception;

/**
 * @description: 默认的异常类型
 * @create: 2025-04-22 01:35
 * @author: TangZhiKai
 **/
public class FrequencyControlException extends RuntimeException{

    public FrequencyControlException(String message) {
        super(message);
    }
}
