package io.github.alfonsokevin.core.limiter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 注解key的类型
 * @create: 2025-04-24 23:37
 * @author: TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum KeyType {
    /**
     * 自定义KEY
     * SpringEL
     */
    KEY("REDIS_FRE_KEY"),
    EL("REDIS_FRE_EL");

    private final String name;

}
