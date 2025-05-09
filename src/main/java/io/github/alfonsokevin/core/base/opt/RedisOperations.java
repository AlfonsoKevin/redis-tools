package io.github.alfonsokevin.core.base.opt;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @description: Redis工具类规范
 * @create: 2025-04-23 10:01
 * @author: TangZhiKai
 **/
public interface RedisOperations {

    /**
     * @see DefaultRedisOperations
     */
    StringRedisTemplate getTemplate();

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
    void multiSet(Map<? extends String, ? extends String> map);

    /**
     * @see DefaultRedisOperations
     */
    Boolean multiSetIfAbsent(Map<? extends String, ? extends String> map);

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


    /**
     * @see DefaultRedisOperations
     */
    void scan(String pattern, Integer count, String charset, Consumer<String> keyHandler);

    /**
     * @see DefaultRedisOperations
     */
    void hset(String key, String field, String value);

    /**
     * @see DefaultRedisOperations
     */
    void hset(String key, String field, Object value);


    /**
     * @see DefaultRedisOperations
     */
    Long hDel(String key, Objects fields);

    /**
     * @see DefaultRedisOperations
     */
    Long hIncrBy(String key, String field, long delta);

    /**
     * @see DefaultRedisOperations
     */
    Double hIncrBy(String key, String field, double delta);

    /**
     * @see DefaultRedisOperations
     */
    Map<String, String> hGetAll(String key);

    /**
     * @see DefaultRedisOperations
     */
    <T> Map<String, T> hGetAll(String key, Class<T> clazz);

    /**
     * @see DefaultRedisOperations
     */
    String hGet(String key, String field);

    /**
     * @see DefaultRedisOperations
     */
    <T> T hGet(String key, String field, Class<T> clazz);

    /**
     * @see DefaultRedisOperations
     */
    Boolean hExists(String key, String field);


    /**
     * @see DefaultRedisOperations
     */
    void hSetNx(String key, String field, String value);

    /**
     * @see DefaultRedisOperations
     */
    Set<String> hKeys(String key);


    /**
     * @see DefaultRedisOperations
     */
    Long hStrLen(String key, String field);

    /**
     * @see DefaultRedisOperations
     */
    void hMSet(String key, Map<String, String> values);

    /**
     * @see DefaultRedisOperations
     */
    List<String> hMGet(String key, Collection<String> fields);

    /**
     * @see DefaultRedisOperations
     */
    Cursor<Map.Entry<String, String>> hScan(String key, ScanOptions scanOptions);

    /**
     * @see DefaultRedisOperations
     */
    void hScan(String key, Consumer<Map.Entry<String, String>> consumer);

    //------------------------- list ------------------------

    /**
     * @see DefaultRedisOperations
     */
    List<String> lRange(String key, long start, long end);

    /**
     * @see DefaultRedisOperations
     */
    void lTrim(String key, long start, long end);

    /**
     * @see DefaultRedisOperations
     */
    Long lLen(String key);

    /**
     * @see DefaultRedisOperations
     */
    Long lPush(String key, String value);

    /**
     * @see DefaultRedisOperations
     */
    Long lPush(String key, Object value);

    /**
     * @see DefaultRedisOperations
     */
    Long lPush(String key, String... values);


    /**
     * @see DefaultRedisOperations
     */
    Long lPush(String key, Object... values);

    /**
     * @see DefaultRedisOperations
     */
    Long lPush(String key, List<Object> values);

    //------------------------- zset ------------------------

    /**
     * @see DefaultRedisOperations
     */
    Boolean zAdd(String key, String value, double score);


    /**
     * @see DefaultRedisOperations
     */
    Boolean zAdd(String key, Object value, double score);


    /**
     * @see DefaultRedisOperations
     */
    Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> valueSet);

    /**
     * @see DefaultRedisOperations
     */
    Long zRem(String key, Object... values);

    /**
     * @see DefaultRedisOperations
     */
    Double zIncrBy(String key, String value, Double delta);

    /**
     * @see DefaultRedisOperations
     */
    Long zRank(String key, Object value);

    /**
     * @see DefaultRedisOperations
     */
    Long zRevRank(String key, Object value);

    /**
     * @see DefaultRedisOperations
     */
    Set<String> zRange(String key, long start, long end);

    /**
     * @see DefaultRedisOperations
     */
    Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start, long end);


    /**
     * @see DefaultRedisOperations
     */
    Set<String> zRangeByScore(String key, double min, double max);

    /**
     * @see DefaultRedisOperations
     */
    Set<String> zRangeByScore(String key, double min, double max, long offset, long count);

    /**
     * @see DefaultRedisOperations
     */
    Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max);

    /**
     * @see DefaultRedisOperations
     */
    Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min,
                                                                   double max, long offset, long count);

    /**
     * @see DefaultRedisOperations
     */
    Set<String> zRevRange(String key, long start, long end);

    /**
     * @see DefaultRedisOperations
     */
    Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScores(String key, long start, long end);

    /**
     * @see DefaultRedisOperations
     */
    Set<String> zRevRangeByScore(String key, double min, double max);

    /**
     * @see DefaultRedisOperations
     */
    Set<String> zRevRangeByScore(String key, double min, double max, long offset, long count);

    /**
     * @see DefaultRedisOperations
     */
    Set<ZSetOperations.TypedTuple<String>> zRevRangeByScoreWithScores(String key,
                                                                      double min,
                                                                      double max,
                                                                      long offset,
                                                                      long count);

    /**
     * @see DefaultRedisOperations
     */
    Long zCount(String key, double min, double max);

    /**
     * @see DefaultRedisOperations
     */
    Long zCard(String key);

    /**
     * @see DefaultRedisOperations
     */
    Long size(String key);

    /**
     * @see DefaultRedisOperations
     */
    Double zScore(String key, Object value);

    /**
     * @see DefaultRedisOperations
     */
    Long zRemRange(String key, long start, long end);


    /**
     * @see DefaultRedisOperations
     */
    Long zRemRangeByScore(String key, double min, double max);

    /**
     * @see DefaultRedisOperations
     */
    Long zUnionStore(String key, String otherKey, String destKey);

    /**
     * @see DefaultRedisOperations
     */
    Long zUnionStore(String key, Collection<String> otherKeys, String destKey);

    /**
     * @see DefaultRedisOperations
     */
    Long zUnionStore(String key,
                     Collection<String> otherKeys,
                     String destKey,
                     RedisZSetCommands.Aggregate aggregate,
                     RedisZSetCommands.Weights weights);

    /**
     * @see DefaultRedisOperations
     */
    Long zInterStore(String key, String otherKey, String destKey);

    /**
     * @see DefaultRedisOperations
     */
    Long zInterStore(String key, Collection<String> otherKeys, String destKey);

    /**
     * @see DefaultRedisOperations
     */
    Long zInterStore(String key,
                     Collection<String> otherKeys,
                     String destKey,
                     RedisZSetCommands.Aggregate aggregate,
                     RedisZSetCommands.Weights weights);

    /**
     * @see DefaultRedisOperations
     */
    Cursor<ZSetOperations.TypedTuple<String>> zScan(String key, ScanOptions scanOptions);


    /**
     * @see DefaultRedisOperations
     */
    void zScan(String key, Consumer<ZSetOperations.TypedTuple<String>> consumer);
}
