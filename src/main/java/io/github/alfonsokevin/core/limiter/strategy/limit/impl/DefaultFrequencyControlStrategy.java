package io.github.alfonsokevin.core.limiter.strategy.limit.impl;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.github.alfonsokevin.core.limiter.annotation.FrequencyControl;
import io.github.alfonsokevin.core.limiter.aspect.FrequencyControlAspect;
import io.github.alfonsokevin.core.limiter.strategy.limit.FrequencyControlStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @description: 默认的限流策略
 * @create: 2025-04-22 01:20
 * @author: TangZhiKai
 **/
@Component(value = "REDIS_FRE_DEFAULT")
@RequiredArgsConstructor
public class DefaultFrequencyControlStrategy implements FrequencyControlStrategy {

    private static final Logger log = LoggerFactory.getLogger(DefaultFrequencyControlStrategy.class);
    private final RedissonClient redissonClient;

    /**
     * 默认最初的实现
     *
     * @param key              默认实现中没有解析SpringEL表达式
     * @param frequencyControl
     * @return
     */
    @Override
    public boolean tryAcquire(String key, FrequencyControl frequencyControl) {
        RRateLimiter redissonRateLimiter = getRedissonRateLimiter(frequencyControl, key);
        return redissonRateLimiter.tryAcquire();
    }

    /**
     * 获取Redisson限流速率器的方法
     * 1.根据key获取限流速率器
     * 2.如果key不存在，直接设置为让注解中设置的就可以了
     * 3.如果key存在，比较配置的参数是否一致，如果不一致，说明进行了修改
     *
     * @param frequencyControl 注解
     * @param currentKey       如果传递了key说明解析了，使用解析后的key，否则使用默认的key
     * @return
     */
    public RRateLimiter getRedissonRateLimiter(FrequencyControl frequencyControl, String currentKey) {
        // 考虑使用哪种key
        RRateLimiter redissonRateLimiter = redissonClient.getRateLimiter(Optional.ofNullable(currentKey).orElse(frequencyControl.key()));
        long rate = frequencyControl.rate();
        long intervalTimes = frequencyControl.intervalTimes();
        TimeUnit unit = frequencyControl.unit();
        if (!redissonRateLimiter.isExists()) {
            // 获取配置好的限定时间intervalTimes下的count次数
            redissonRateLimiter.trySetRate(RateType.OVERALL, rate, unit.toMillis(intervalTimes), RateIntervalUnit.MILLISECONDS);
            return redissonRateLimiter;
        }
        // 如果存在，就需要获取原有的配置和现在的配置是否一样
        RateLimiterConfig config = redissonRateLimiter.getConfig();
        long originalRateInterval = config.getRateInterval();
        long originRate = config.getRate();
        if (TimeUnit.MILLISECONDS.convert(intervalTimes, frequencyControl.unit()) != originalRateInterval
                || rate != originRate) {
            // 如果重启过，使用的原有的配置，将其删除修改为最新的配置
            redissonRateLimiter.delete();
            redissonRateLimiter
                    .trySetRate(RateType.OVERALL, rate, unit.toMillis(intervalTimes), RateIntervalUnit.MILLISECONDS);
        }
        log.debug("[{FrequencyControl}]: >> 限流速率器的配置为，使用的策略是: {} ,{} 毫秒 之内允许点击{}次数",
                redissonRateLimiter.getConfig().getRateType(), redissonRateLimiter.getConfig().getRateInterval(),
                redissonRateLimiter.getConfig().getRate());
        return redissonRateLimiter;
    }

    /**
     * 获取Redisson限流速率器的方法 不解析key，使用原注解中的key
     *
     * @return 限流速率器
     */
    public RRateLimiter getRedissonRateLimiter(FrequencyControl frequencyControl) {
        return this.getRedissonRateLimiter(frequencyControl, null);
    }
}
