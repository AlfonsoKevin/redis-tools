package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 标准错误码
 * @since 2025-04-28 11:57
 * @author TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum StandardResultCode implements ResultCode {
    //
    NAME_DISCOVER_PARSE_FAILED(40103, "NameDiscover解析失败~~"),
    SCHEDULED_FAILED(40102, "执行定时任务失败"),
    REQUEST_PARAMETER_EXCEPTION(40400, "请求参数异常"),
    REQUEST_PARAMETER_IS_NULL(40401, "请求参数为空"),
    POINTCUT_METHOD_FAILED(40101, "获取切入点方法失败"),
    INITIALIZATION_ERROR(40100,"初始化错误"),

    SUCCESS(20000, "成功"),
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
