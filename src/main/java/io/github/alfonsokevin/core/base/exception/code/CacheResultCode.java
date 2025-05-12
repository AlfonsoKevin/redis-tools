package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 自定义错误码
 * @since 2025-04-28 11:59
 * @author TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum CacheResultCode implements ResultCode {
    // 都是RedisCacheable部分的错误码
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
