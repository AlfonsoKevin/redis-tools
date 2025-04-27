package io.github.alfonsokevin.core.cache.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 缓存Key生成的策略
 * @create: 2025-04-24 16:19
 * @author: TangZhiKai
 **/
@AllArgsConstructor
@Getter
public enum KeyType {
    /**
     * DEFAULT 默认策略
     * EL EL表达式
     */
    DEFAULT,
    EL;

}
