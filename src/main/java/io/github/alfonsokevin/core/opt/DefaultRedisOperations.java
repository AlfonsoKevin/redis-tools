package io.github.alfonsokevin.core.opt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
     * @return 操作类
     */
    @Override
    public StringRedisTemplate template() {
        return this.TEMPLATE;
    }

    /**
     * string
     *
     * @return 操作类
     */
    @Override
    public ValueOperations<String, String> string() {
        return TEMPLATE.opsForValue();
    }

    /**
     * hash
     *
     * @return 操作类
     */
    @Override
    public HashOperations<String, String, String> hash() {
        return TEMPLATE.opsForHash();
    }

    /**
     * list
     *
     * @return 操作类
     */
    @Override
    public ListOperations<String, String> list() {
        return TEMPLATE.opsForList();
    }

    /**
     * set
     *
     * @return 操作类
     */
    @Override
    public SetOperations<String, String> set() {
        return TEMPLATE.opsForSet();
    }

    /**
     * zset
     *
     * @return 操作类
     */
    @Override
    public ZSetOperations<String, String> zset() {
        return TEMPLATE.opsForZSet();
    }

    /**
     * set 方法
     *
     * @param key   字符串
     * @param value 字符串
     */
    @Override
    public void set(String key, String value) {
        string().set(key, value);
    }

    /**
     * set 方法
     *
     * @param key   字符串
     * @param value 字符串
     */
    @Override
    public void set(String key, Object value) {
        string().set(key, parseToString(value));
    }

    /**
     * 命令用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始。
     *
     * @param key    key
     * @param value  指定字符串value
     * @param offset 偏移量
     */
    @Override
    public void setRange(String key, String value, long offset) {
        string().set(key, value, offset);
    }

    /**
     * 判断当前key是否存在
     *
     * @param key key
     * @return 如果存在返回true，否则返回false
     */
    @Override
    public Boolean exists(String key) {
        return TEMPLATE.hasKey(key);
    }

    /**
     * 设置key的过期时间 key不存在或者不能设置的时候，返回false
     *
     * @param key     key
     * @param timeout 过期时间
     * @param unit    单位
     * @return key不存在或者不能设置的时候，返回false
     */
    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return TEMPLATE.expire(key, timeout, unit);
    }

    /**
     * 默认的可以使用秒作为单位设置
     *
     * @param key
     * @param timeout 多少秒后过期
     * @return
     */
    @Override
    public Boolean expire(String key, long timeout) {
        return TEMPLATE.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 判断当前key是否过期
     *
     * @param key key
     * @return 如果没有过期，返回剩余的过期时间，否则返回-1
     */
    @Override
    public long ttl(String key) {
        return Optional.ofNullable(TEMPLATE.getExpire(key)).orElse(-1L);
    }


    /**
     * 设置key的过期时间 key不存在或者不能设置的时候，返回false
     *
     * @param key     key
     * @param timeout 计算两个“时间”间隔的类
     * @return
     */
    @Override
    public Boolean expire(String key, Duration timeout) {
        Assert.notNull(timeout, "[{Redis-Base-Utils}]: >> The param of Duration mast not be null");
        // 判断返回的类型是否是毫秒值
        return TimeoutUtils.hasMillis(timeout) ? expire(key, timeout.toMillis(), TimeUnit.MILLISECONDS)
                : expire(key, timeout.getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * Redis原生命令
     * 尝试设置值，只有key不存在才会设置成功
     * 如果 key 不存在，设置 key 的值为 value，并返回 1（成功）
     * 如果 key 已存在，不执行任何操作，返回 0（失败）。
     *
     * @param key
     * @param value
     */
    @Override
    public Boolean setNx(String key, String value) {
        return string().setIfAbsent(key, value);
    }

    /**
     * 如果不存在就设置值返回true，否则就返回false
     *
     * @param key
     * @param value
     * @return true/false
     */
    @Override
    public Boolean setIfAbsent(String key, String value) {
        return string().setIfAbsent(key, value);
    }

    /**
     * 如果不存在就设置值返回true，否则就返回false
     *
     * @param key
     * @param value
     * @return true/false
     */
    @Override
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        return string().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 如果不存在就设置值返回true，否则就返回false
     *
     * @param key     key
     * @param value   value
     * @param timeout Duration.ofSeconds()
     * @return true/false
     */
    @Override
    public Boolean setIfAbsent(String key, String value, Duration timeout) {
        return string().setIfAbsent(key, value, timeout);
    }

    /**
     * get 方法
     *
     * @param key 字符串
     * @return value
     */
    @Override
    public String get(String key) {
        return string().get(key);
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
     * @param key   key
     * @param value value
     * @return
     */
    @Override
    public String getSet(String key, String value) {
        return string().getAndSet(key, value);
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
        return Optional.ofNullable(string().getAndSet(key, parseToString(value)))
                .map(v -> JSON.parseObject(v, clazz))
                .orElse(null);
    }

    /**
     * 获取指定key对应value再offset偏移量上的bit(位)
     * # 对不存在的 key 或者不存在的 offset 进行 getBit， 返回 0
     * # 对已存在的 offset 进行 getBit
     * @param key key
     * @param offset 偏移量
     * @return 如果value的offset偏移量上存在返回true，否则返回false
     */
    @Override
    public Boolean getBit(String key, long offset) {
        return string().getBit(key, offset);
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
     * @param key key
     * @return
     */
    @Override
    public Long incr(String key) {
        return string().increment(key);
    }

    /**
     * incrby根据指定值做递增或递减操作并返回递增或递减后的结果
     *
     * @param key   key
     * @param delta 指定递增值
     * @return
     */
    @Override
    public Long incrBy(String key, long delta) {
        return string().increment(key, delta);
    }

    /**
     * incrby根据指定值做递增或递减操作并返回递增或递减后的结果
     *
     * @param key   key
     * @param delta 指定递增值
     * @return
     */
    @Override
    public Double incrBy(String key, double delta) {
        return string().increment(key, delta);
    }

    @Override
    public Long decr(String key) {
        return string().decrement(key);
    }

    @Override
    public Long decrBy(String key, long delta) {
        return string().decrement(key, delta);
    }

    @Override
    public List<String> mget(Collection<String> key) {
        return string().multiGet(key);
    }

    @Override
    public <T> List<T> mget(Collection<String> key, Class<T> clazz) {
        return Optional.ofNullable(string().multiGet(key))
                .map(v -> parseToObject(key, clazz))
                .orElse(new ArrayList<>());
    }

    /**
     * 同时为多个键key设置值。
     *
     * @param keys 多个key
     */
    @Override
    public void mset(Map<String, String> keys) {
        string().multiSet(keys);
    }


    /**
     * 同时为多个key尝试设置值，只有key不存在才会设置成功
     *
     * @param keys
     */
    @Override
    public void msetNx(Map<String, String> keys) {
        string().multiSetIfAbsent(keys);
    }


    /**
     * 把列表中的对象转换成需要的类型
     *
     * @param valueList value的列表集合
     * @param clazz     需要的类型
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
     * @param value 值
     * @param <T>   传递的类型
     * @return 字符串类型
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
