package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.exception.FrequencyControlBuilder;
import io.github.alfonsokevin.core.limiter.exception.FrequencyControlException;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @description: 滑动窗口策略限流
 * @create: 2025-04-22 01:21
 * @author: TangZhiKai
 **/
@Component(value = "REDIS_FREQ_SLIDING_WINDOW")
@RequiredArgsConstructor //将所有final字段构造函数中初始化
public class SlidingWindowFrequencyControlStrategy implements FrequencyControlStrategy {

    private static final Logger log = LoggerFactory.getLogger(SlidingWindowFrequencyControlStrategy.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final ResourceLoader resourceLoader;
    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) throws FrequencyControlException {
        // 时间单位换算为毫秒
        long interval = frequencyControl.unit().toMillis(frequencyControl.intervalTimes());
        long maxRequest = frequencyControl.rate();

        String luaScript;
        try {
            luaScript = loadScript("lua/sliding_window.lua");
        }catch (Exception e){
            log.error("[{FrequencyControl}]: >> load lua script failed~~",e);
            throw  FrequencyControlBuilder.build(frequencyControl.exceptionClass(), frequencyControl.message());
        }
        // 当前时间戳（毫秒）
        long now = System.currentTimeMillis();

        // 调用 Redis 执行 Lua 脚本（假设你用的是 Redisson 或 Jedis）
        Object result = stringRedisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Long.class),
                Collections.singletonList(key),
                String.valueOf(now),
                String.valueOf(interval),
                String.valueOf(maxRequest)
        );

        return Long.valueOf(1).equals(result);
    }

    /**
     * 使用ResourceLoader读取Lua脚本的方式
     * @param path 类路径下的路径
     * @return 读取到的Lua脚本
     * @throws Exception
     */
    @Override
    public String loadScript(String path) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
