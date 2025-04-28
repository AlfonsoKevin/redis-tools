package io.github.alfonsokevin.core.base.exception.code;

/**
 * @description: redistools 异常错误码
 * @create: 2025-04-28 11:54
 * @author: TangZhiKai
 **/
public interface ResultCode {
    /**
     * 错误码描述
     * @return 错误码
     */
    int getCode();

    /**
     * 错误码描述
     * @return 错误码描述
     */
    String getMessage();
}
