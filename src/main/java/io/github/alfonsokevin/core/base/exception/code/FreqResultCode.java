package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 速率限流器的自定义异常枚举
 * @since 2025-04-28 23:27
 * @author TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum FreqResultCode implements ResultCode {
    //
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
