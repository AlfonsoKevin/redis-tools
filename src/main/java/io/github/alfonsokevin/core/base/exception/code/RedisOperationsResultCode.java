package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TangZhiKai
 * @description Redis基础工具类的错误码
 * @since 2025-05-12 23:12
 **/
@AllArgsConstructor
@Getter
public enum RedisOperationsResultCode implements ResultCode{
    SCAN_EXCEPTION(40410,"根据游标迭代异常"),
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
