package io.github.alfonsokevin.core.opt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: Redis默认的工具类实现
 * @create: 2025-04-23 10:02
 * @author: TangZhiKai
 **/
@Component
public class DefaultRedisOperations implements RedisOperations {

    private final StringRedisTemplate TEMPLATE;

    public DefaultRedisOperations(StringRedisTemplate redisTemplate) {
        this.TEMPLATE = redisTemplate;
    }

    /**
     * 对外提供 StringRedisTemplate
     *
     * @return
     */
    @Override
    public StringRedisTemplate template() {
        return this.TEMPLATE;
    }

    /**
     * 操作方法
     *
     * @return
     */
    @Override
    public ValueOperations<String, String> ops() {
        return TEMPLATE.opsForValue();
    }


    /**
     * set 方法
     *
     * @param key   字符串
     * @param value 字符串
     */
    @Override
    public void set(String key, String value) {
        ops().set(key, value);
    }

    /**
     * set 方法
     *
     * @param key   字符串
     * @param value 字符串
     */
    @Override
    public void set(String key, Object value) {
        ops().set(key, parseToString(value));
    }

    /**
     * Redis原生命令
     * 尝试设置值，只有key不存在才会设置成功
     * 如果 key 不存在，设置 key 的值为 value，并返回 1（成功）
     * 如果 key 已存在，不执行任何操作，返回 0（失败）。
     * @param key
     * @param value
     */
    @Override
    public Boolean setNx(String key, String value) {
        return ops().setIfAbsent(key, value);
    }

    /**
     * 如果不存在就设置值返回true，否则就返回false
     * @param key
     * @param value
     * @return true/false
     */
    @Override
    public Boolean setIfAbsent(String key, String value) {
        return ops().setIfAbsent(key, value);
    }

    /**
     * 如果不存在就设置值返回true，否则就返回false
     * @param key
     * @param value
     * @return true/false
     */
    @Override
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        return ops().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 如果不存在就设置值返回true，否则就返回false
     * @param key
     * @param value
     * @param timeout Duration.ofSeconds()
     * @return true/false
     */
    @Override
    public Boolean setIfAbsent(String key, String value, Duration timeout) {
        return ops().setIfAbsent(key, value, timeout);
    }

    /**
     * get 方法
     *
     * @param key 字符串
     * @return value
     */
    @Override
    public String get(String key) {
        return ops().get(key);
    }

    /**
     * get 方法-返回指定的value
     *
     * @param key   字符串
     * @param clazz 想要转换的字节码类型
     * @return value
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        return Optional.ofNullable(get(key))
                .map(v -> JSON.parseObject(key, clazz))
                .orElse(null);
    }


    /**
     * 如果获取不到值就设置，最后返回
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String getSet(String key, String value) {
        return ops().getAndSet(key, value);
    }

    /**
     * 如果获取不到值就设置，最后返回
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public <T> T getSet(String key, T value, Class<T> clazz) {
        return Optional.ofNullable(ops().getAndSet(key, parseToString(value)))
                .map(v -> JSON.parseObject(v, clazz))
                .orElse(null);
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    @Override
    public Boolean del(String key) {
        return this.template().delete(key);
    }


    /**
     * 原子递增1并返回递增后的结果；
     *
     * @param key
     * @return
     */
    @Override
    public Long incr(String key) {
        return ops().increment(key);
    }

    /**
     * incrby根据指定值做递增或递减操作并返回递增或递减后的结果
     *
     * @param key
     * @param delta 指定递增值
     * @return
     */
    @Override
    public Long incrBy(String key, long delta) {
        return ops().increment(key, delta);
    }

    /**
     * incrby根据指定值做递增或递减操作并返回递增或递减后的结果
     *
     * @param key
     * @param delta 指定递增值
     * @return
     */
    @Override
    public Double incrBy(String key, double delta) {
        return ops().increment(key, delta);
    }

    @Override
    public Long decr(String key) {
        return ops().decrement(key);
    }

    @Override
    public Long decrBy(String key, long delta) {
        return ops().decrement(key, delta);
    }

    @Override
    public List<String> mget(Collection<String> key) {
        return ops().multiGet(key);
    }

    @Override
    public <T> List<T> mget(Collection<String> key, Class<T> clazz) {
        return Optional.ofNullable(ops().multiGet(key))
                .map(v -> parseToObject(key, clazz))
                .orElse(new ArrayList<>());
    }

    /**
     * 同时为多个键key设置值。
     *
     * @param keys
     */
    @Override
    public void mset(Map<String, String> keys) {
        ops().multiSet(keys);
    }


    @Override
    public void msetNx(Map<String, String> keys) {
        ops().multiSetIfAbsent(keys);
    }

    /**
     * 把列表中的对象转换成需要的类型
     *
     * @param valueList value的列表集合
     * @param clazz
     * @param <T>       需要的类型
     * @return
     * @see this#mget(Collection, Class)
     */
    private <T> List<T> parseToObject(Collection<String> valueList, Class<T> clazz) {
        return valueList.stream().map(v -> JSON.parseObject(v, clazz)).collect(Collectors.toList());
    }


    /**
     * 解析成字符串类型
     *
     * @param value
     * @param <T>
     * @return
     */
    private <T> String parseToString(T value) {
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof Integer ||
                value instanceof Double ||
                value instanceof Long ||
                value instanceof Float) {
            return String.valueOf(value);
        }

        return JSONObject.toJSONString(value);
    }

}
