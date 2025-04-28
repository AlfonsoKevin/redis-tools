package io.github.alfonsokevin.core.limiter.strategy.limit;

import io.github.alfonsokevin.core.base.exception.impl.FrequencyControlException;
import io.github.alfonsokevin.core.limiter.model.FrequencyControl;

/**
 * @description: 限流策略
 * @create: 2025-04-22 01:13
 * @author: TangZhiKai
 **/
public interface FrequencyControlStrategy {
    /**
     *
     * @param key 解析好的key
     * @param frequencyControl 速率限流器注解
     * @return
     */
    boolean tryAcquire(String key, FrequencyControl frequencyControl) throws FrequencyControlException;


    /**
     * 使用ResourceLoader读取Lua脚本的方式
     * @param path 类路径下的路径
     * @return 读取到的Lua脚本
     * @throws Exception
     */
    default String loadScript(String path) throws Exception {
        return "";
    }
}
