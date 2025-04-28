package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 自定义错误码
 * @create: 2025-04-28 11:59
 * @author: TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum CacheResultCode implements ResultCode {
    // 都是RedisCacheable部分的错误码
    CACHE_REQUEST_PARAMETER_EXCEPTION(40400, "请求参数异常"),
    CACHE_REQUEST_PARAMETER_IS_NULL(40401, "请求参数为空"),
    CACHE_INITIALIZATION_ERROR(40100,"初始化错误"),
    CACHE_POINTCUT_METHOD_FAILED(40101, "获取切入点方法失败"),
    CACHE_SCHEDULED_FAILED(40102, "执行定时任务失败"),
    CACHE_NAME_DISCOVER_PARSE_FAILED(40103, "NameDiscover解析失败~~"),
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
