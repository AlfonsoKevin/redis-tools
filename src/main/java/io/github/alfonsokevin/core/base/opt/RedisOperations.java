package io.github.alfonsokevin.core.base.opt;

import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: Redis工具类规范
 * @create: 2025-04-23 10:01
 * @author: TangZhiKai
 **/
public interface RedisOperations {

    /**
     * @see DefaultRedisOperations
     */
    StringRedisTemplate template();

    /**
     * @see DefaultRedisOperations
     */
    ValueOperations<String, String> string();

    /**
     * @see DefaultRedisOperations
     */
    HashOperations<String, String, String> hash();

    /**
     * @see DefaultRedisOperations
     */
    ListOperations<String, String> list();

    /**
     * @see DefaultRedisOperations
     */
    SetOperations<String, String> set();

    /**
     * @see DefaultRedisOperations
     */
    ZSetOperations<String, String> zset();

    /**
     * @see DefaultRedisOperations
     */
    void set(String key, String value);

    /**
     * @see DefaultRedisOperations
     */
    void set(String key, Object value);

    /**
     * @see DefaultRedisOperations
     */
    void setRange(String key, String value, long offset);

    /**
     * @see DefaultRedisOperations
     */
    Boolean exists(String key);

    /**
     * @see DefaultRedisOperations
     */
    Boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * @see DefaultRedisOperations
     */
    Boolean expire(String key, long timeout);

    /**
     * @see DefaultRedisOperations
     */
    Boolean expire(String key, Duration timeout);

    /**
     * @see DefaultRedisOperations
     */
    long ttl(String key);

    /**
     * @see DefaultRedisOperations
     */
    Boolean setNx(String key, String value);

    /**
     * @see DefaultRedisOperations
     */
    Boolean setIfAbsent(String key, String value);

    /**
     * @see DefaultRedisOperations
     */
    Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit);

    /**
     * @see DefaultRedisOperations
     */
    Boolean setIfAbsent(String key, String value, Duration timeout);

    /**
     * @see DefaultRedisOperations
     */
    String get(String key);

    /**
     * @see DefaultRedisOperations
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * @see DefaultRedisOperations
     */
    String getSet(String key, String value);

    /**
     * @see DefaultRedisOperations
     */
    <T> T getSet(String key, T value, Class<T> clazz);

    /**
     * @see DefaultRedisOperations
     */
    Boolean getBit(String key, long offset);

    /**
     * @see DefaultRedisOperations
     */
    Boolean del(String key);

    /**
     * @see DefaultRedisOperations
     */
    Long incr(String key);

    /**
     * @see DefaultRedisOperations
     */
    Long incrBy(String key, long delta);

    /**
     * @see DefaultRedisOperations
     */
    Double incrBy(String key, double delta);

    /**
     * @see DefaultRedisOperations
     */
    Long decr(String key);

    /**
     * @see DefaultRedisOperations
     */
    Long decrBy(String key, long delta);

    /**
     * @see DefaultRedisOperations
     */
    List<String> mget(Collection<String> key);

    /**
     * @see DefaultRedisOperations
     */
    <T> List<T> mget(Collection<String> key, Class<T> clazz);

    /**
     * @see DefaultRedisOperations
     */
    void mset(Map<String, String> keys);

    /**
     * @see DefaultRedisOperations
     */
    void msetNx(Map<String, String> keys);

}
