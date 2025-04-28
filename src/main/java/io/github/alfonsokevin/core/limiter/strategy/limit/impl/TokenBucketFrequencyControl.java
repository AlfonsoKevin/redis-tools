package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import io.github.alfonsokevin.core.limiter.model.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: 令牌桶限流策略
 * @create: 2025-04-22 01:13
 * @author: TangZhiKai
 **/
@Component("REDIS_FREQ_TOKEN_BUCKET")
@RequiredArgsConstructor
public class TokenBucketFrequencyControl implements FrequencyControlStrategy {
    private static final Logger log = LoggerFactory.getLogger(TokenBucketFrequencyControl.class);
    private final StringRedisTemplate stringRedisTemplate;
    private final ResourceLoader resourceLoader;

    /**
     * TODO 套一层抽象类，将特有的部分延迟实例化
     * @param key              解析好的key
     * @param frequencyControl 速率限流器注解
     * @return
     */
    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) {
        // String luaScript;
        // try {
        //     luaScript = loadScript("lua/token_bucket.lua");
        // } catch (Exception e) {
        //     log.error("[{FrequencyControl}]: >> load lua script failed~~", e);
        //     throw FrequencyControlBuilder.build(frequencyControl.getExceptionClass(), frequencyControl.getMessage());
        // }
        // DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        // script.setScriptText(luaScript);
        // script.setResultType(Long.class);
        //
        // long intervalMillis = frequencyControl.getUnit().toMillis(frequencyControl.getIntervalTimes());
        // long currentTimeMillis = System.currentTimeMillis();
        //
        // List<String> keys = Collections.singletonList(key);
        // List<String> args = Arrays.asList(
        //         // 生成一个令牌所需间隔
        //         String.valueOf(intervalMillis / frequencyControl.getRate()),
        //         // 当前时间戳
        //         String.valueOf(currentTimeMillis),
        //         // 初始化令牌数
        //         String.valueOf(frequencyControl.getCapacity()),
        //         // 桶最大容量
        //         String.valueOf(frequencyControl.getCapacity()),
        //         // 重置时间间隔
        //         String.valueOf(intervalMillis)
        // );
        //
        // Long result = stringRedisTemplate.execute(script, keys, args.toArray());
        // return result != null && result > 0;
        return false;
    }

    @Override
    public String loadScript(String path) throws Exception {
        return "";
    }
}
