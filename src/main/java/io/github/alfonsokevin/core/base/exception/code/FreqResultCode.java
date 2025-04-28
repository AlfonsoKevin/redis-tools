package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 速率限流器的自定义异常枚举
 * @create: 2025-04-28 23:27
 * @author: TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum FreqResultCode implements ResultCode {
    //
    FREQ_REQUEST_PARAMETER_EXCEPTION(40400, "请求参数异常"),
    FREQ_REQUEST_PARAMETER_IS_NULL(40401, "请求参数为空"),
    FREQ_INITIALIZATION_ERROR(40100,"初始化错误"),
    FREQ_POINTCUT_METHOD_FAILED(40101, "获取切入点方法失败"),
    FREQ_SCHEDULED_FAILED(40102, "执行定时任务失败"),
    ;
    private final int code;
    private final String msg;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
