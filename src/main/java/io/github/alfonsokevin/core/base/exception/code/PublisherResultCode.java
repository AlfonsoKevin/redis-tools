package io.github.alfonsokevin.core.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TangZhiKai
 * @description description
 * @since 2025/5/12
 **/
@AllArgsConstructor
@Getter
public enum PublisherResultCode implements ResultCode {
    // 错误码
    PUBLISHER_SIDE_REQUEST_PARAMETER_OUT_OF_ALLOWED_RANGE(40402, "请求参数值超出允许的范围"),
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
