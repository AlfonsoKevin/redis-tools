package io.github.alfonsokevin.core.cache.model.enums;

/**
 * @description CacheEvict生成key的类型
 * @since 2025-04-27 14:59
 * @author TangZhiKai
 **/
public enum EvictKeyType {
    /**
     * DEFAULT 默认策略
     * EL EL表达式
     * RESULT 获取到方法执行结果，清除该key
     */
    DEFAULT,
    EL,
    RESULT
    ;
}
