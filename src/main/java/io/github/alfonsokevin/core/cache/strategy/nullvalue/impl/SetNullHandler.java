package io.github.alfonsokevin.core.cache.strategy.nullvalue.impl;

import io.github.alfonsokevin.core.cache.model.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.NullType;
import io.github.alfonsokevin.core.cache.strategy.nullvalue.NullValueHandler;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @description: 设置空值的策略类
 * @create: 2025-04-23 19:24
 * @author: TangZhiKai
 **/
@Component
public class SetNullHandler implements NullValueHandler {

    private final static Logger log = LoggerFactory.getLogger(SetNullHandler.class);
    private final StringRedisTemplate stringRedisTemplate;

    public SetNullHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 处理空值的策略
     * TODO 使用基础工具类
     *
     * @param cacheable
     * @param key       解析后的key
     * @return
     */
    @Override
    public void handleNullValue(RedisCacheable cacheable, String key) { // 需要传入key
        String value = stringRedisTemplate.opsForValue().get(key);
        if (Objects.nonNull(value)) {
            // 不为空直接返回了
            return ;
        }
        log.warn("[{RedisCacheable}]: >> Use Strategy: {}, will be set null value"
                , cacheable.getNullType().toString());
        // 缓存空值，计算缓存空值的时间
        long planNullTime = planNullTime(cacheable.getExpireTime(), cacheable.getUnit());
        stringRedisTemplate.opsForValue().set(key, null, planNullTime, TimeUnit.MILLISECONDS);
        // TODO 考虑是否要把null加入到一个集合中什么的
        return ;
    }

    /**
     * 缓存空值的时间 原有时间的20% + 原有时间5%范围内的随机
     *
     * @return
     */
    private long planNullTime(long originalTime, TimeUnit unit) {
        long millis = unit.toMillis(originalTime);
        BigDecimal mainPart = new BigDecimal(millis);
        BigDecimal randomPart = new BigDecimal(millis);
        long randomRange = RandomUtils.nextLong(0, randomPart.multiply(BigDecimal.valueOf(0.05))
                .setScale(0, RoundingMode.UP).longValue() + 1);
        return mainPart.multiply(BigDecimal.valueOf(0.2)).add(new BigDecimal(randomRange)).longValue();
    }

    @Override
    public NullType getNullHandlerType() {
        return NullType.SET_NULL;
    }
}
