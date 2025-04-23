package io.github.alfonsokevin.core.opt;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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

    StringRedisTemplate template();

    ValueOperations<String, String> ops();

    void set(String key, String value);

    void set(String key, Object value);

    Boolean setNx(String key, String value);

    Boolean setIfAbsent(String key, String value);

    Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit);

    Boolean setIfAbsent(String key, String value, Duration timeout);

    String get(String key);

    <T> T get(String key, Class<T> clazz);

    String getSet(String key, String value);

    <T> T getSet(String key, T value, Class<T> clazz);

    Boolean del(String key);

    Long incr(String key);

    Long incrBy(String key, long delta);

    Double incrBy(String key, double delta);

    Long decr(String key);

    Long decrBy(String key, long delta);

    List<String> mget(Collection<String> key);

    <T> List<T> mget(Collection<String> key, Class<T> clazz);

    void mset(Map<String, String> keys);

    void msetNx(Map<String, String> keys);


}
