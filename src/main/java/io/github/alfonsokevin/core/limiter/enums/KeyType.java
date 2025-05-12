package io.github.alfonsokevin.core.limiter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 注解key的类型
 * @since 2025-04-24 23:37
 * @author TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum KeyType {
    /**
     * 自定义KEY
     * SpringEL
     * 请求的IP地址
     */
    KEY,
    EL,
    IP;

}
