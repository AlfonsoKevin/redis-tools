package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 标准错误码
 * @create: 2025-04-28 11:57
 * @author: TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum StandardResultCode implements ResultCode {
    //
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
