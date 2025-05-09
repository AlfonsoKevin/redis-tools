package io.github.alfonsokevin.core.base.opt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @description: Redis默认的工具类实现
 * @create: 2025-04-23 10:02
 * @author: TangZhiKai
 **/
@Component
public class DefaultRedisOperations implements RedisOperations {

    private final static Logger log = LoggerFactory.getLogger(DefaultRedisOperations.class);

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
    public StringRedisTemplate getTemplate() {
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
     * 批量设置
     *
     * @param map
     */
    @Override
    public void multiSet(Map<? extends String, ? extends String> map) {
        getTemplate().opsForValue().multiSet(map);
    }

    /**
     * 批量设置不存在的部分
     *
     * @param map
     * @return
     */
    @Override
    public Boolean multiSetIfAbsent(Map<? extends String, ? extends String> map) {
        return string().multiSetIfAbsent(map);
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
                .map(v -> JSONObject.parseObject(key, clazz))
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
     *
     * @param key    key
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
        return this.getTemplate().delete(key);
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
     * 通过游标分批次迭代，实现非阻塞式遍历
     *
     * @param pattern    匹配的前缀
     * @param count      返回游标行数
     * @param charset    charset
     * @param keyHandler keyHandler
     */
    @Override
    public void scan(String pattern, Integer count, String charset, Consumer<String> keyHandler) {
        getTemplate().execute((RedisCallback<Set<String>>) connection -> {
            try (Cursor<byte[]> cursor = connection.keyCommands().scan(ScanOptions.scanOptions()
                    .match(pattern)
                    .count(Objects.isNull(count) ? 1000 : count)
                    .build())) {
                while (cursor.hasNext()) {
                    keyHandler.accept(new String(cursor.next(), charset));
                }
            } catch (Exception e) {
                log.error("[{Redis STRING}]: >> {} ", e.getMessage(), e);
                //
                throw new RuntimeException(e);
            }

            return null;
        });

    }

    //------------------------- hash ------------------------


    /**
     * hash类型 set
     *
     * @param key   key
     * @param field field
     * @param value value
     */
    @Override
    public void hset(String key, String field, String value) {
        hash().put(key, field, value);
    }

    /**
     * hash 类型 set
     *
     * @param key   key
     * @param field field
     * @param value value
     */
    @Override
    public void hset(String key, String field, Object value) {
        hash().put(key, field, parseToString(value));
    }

    /**
     * hash 删除
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public Long hDel(String key, Objects fields) {
        return hash().delete(key, fields);
    }

    /**
     * hash incrby 用于为哈希表中的字段值加上指定增量值
     * 增量也可以为负数，相当于对指定字段进行减法操作。
     *
     * @param key   key
     * @param field field
     * @param delta delta
     * @return 执行 HINCRBY 命令之后，哈希表中字段的值。
     */
    @Override
    public Long hIncrBy(String key, String field, long delta) {
        return hash().increment(key, field, delta);
    }

    /**
     * hash incrby 用于为哈希表中的字段值加上指定增量值
     * 增量也可以为负数，相当于对指定字段进行减法操作。
     *
     * @param key   key
     * @param field field
     * @param delta delta
     * @return 执行 HINCRBY 命令之后，哈希表中字段的值。
     */
    @Override
    public Double hIncrBy(String key, String field, double delta) {
        return hash().increment(key, field, delta);
    }

    /**
     * hash mget
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, String> hGetAll(String key) {
        return hash().entries(key);
    }

    /**
     * hash mget
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> Map<String, T> hGetAll(String key, Class<T> clazz) {
        return Optional.of(hash().entries(key))
                .map(map -> {
                    Map<String, T> result = new HashMap<>(map.size());
                    map.forEach((k, v) -> result.put(k, parseToObject(v, clazz)));
                    return result;
                })
                .orElse(new HashMap<>());
    }

    /**
     * hget
     * 获取存储在哈希表中指定字段的值。
     *
     * @param key   key
     * @param field field
     * @return string
     */
    @Override
    public String hGet(String key, String field) {
        return hash().get(key, field);
    }

    /**
     * hget 获取存储在哈希表中指定字段的值,返回需要的类型
     *
     * @param key   key
     * @param field field
     * @param clazz clazz
     * @param <T>   <T>
     * @return T
     */
    @Override
    public <T> T hGet(String key, String field, Class<T> clazz) {
        return Optional.ofNullable(hash().get(key, field))
                .map(v -> parseToObject(v, clazz))
                .orElse(null);
    }

    /**
     * Redis Hexists 命令用于查看哈希表的指定字段是否存在。
     *
     * @param key   key
     * @param field field
     * @return TRUE/FALSE
     */
    @Override
    public Boolean hExists(String key, String field) {
        return hash().hasKey(key, field);
    }

    /**
     * 只有在字段 field 不存在时，设置哈希表字段的值。
     *
     * @param key   key
     * @param field field
     * @param value value
     */
    @Override
    public void hSetNx(String key, String field, String value) {
        hash().putIfAbsent(key, field, value);
    }

    /**
     * 获取哈希表中的所有字段
     *
     * @param key key
     * @return Set<String>
     */
    @Override
    public Set<String> hKeys(String key) {
        return hash().keys(key);
    }

    /**
     * 返回哈希表中给定域相关联的值的字符串长度
     *
     * @param key   key
     * @param field field
     * @return - 如果给定的键或者域不存在， 返回 0
     * - 如果 key 不存在时，返回 0
     */
    @Override
    public Long hStrLen(String key, String field) {
        return hash().lengthOfValue(key, field);
    }


    /**
     * 批量给域设置 field-value
     *
     * @param key    key
     * @param values values
     */
    @Override
    public void hMSet(String key, Map<String, String> values) {
        hash().putAll(key, values);
    }

    /**
     * Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。
     * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
     *
     * @param key    key
     * @param fields fields
     * @return fields关联的值values
     */
    @Override
    public List<String> hMGet(String key, Collection<String> fields) {
        return hash().multiGet(key, fields);
    }

    /**
     * Redis HSCAN 命令用于迭代哈希表中的键值对。
     *
     * @param key         key
     * @param scanOptions scanOptions
     * @return Cursor
     */
    @Override
    public Cursor<Map.Entry<String, String>> hScan(String key, ScanOptions scanOptions) {
        return hash().scan(key, scanOptions);
    }

    /**
     * Redis HSCAN 命令用于迭代哈希表中的键值对。
     *
     * @param key      key
     * @param consumer consumer
     */
    @Override
    public void hScan(String key, Consumer<Map.Entry<String, String>> consumer) {
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match("*")
                .count(10000)
                .build();
        try (Cursor<Map.Entry<String, String>> cursor = hash().scan(key, scanOptions)) {
            while (cursor.hasNext()) {
                consumer.accept(cursor.next());
            }
        } catch (Exception e) {
            log.error("[{Redis HSCAN}]: >> {} ", e.getMessage(), e);
        }
    }

    //------------------------- list ------------------------

    /**
     * Redis Lrange 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * [start,end]
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 一个列表，包含指定区间内的元素。
     */
    @Override
    public List<String> lRange(String key, long start, long end) {
        return list().range(key, start, end);
    }

    /**
     * Redis Ltrim 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key   key
     * @param start start
     * @param end   end
     */
    @Override
    public void lTrim(String key, long start, long end) {
        list().trim(key, start, end);
    }

    /**
     * 返回列表中元素的个数
     *
     * @param key key
     * @return Long
     */
    @Override
    public Long lLen(String key) {
        return list().size(key);
    }

    /**
     * Redis Lpush 命令将一个或多个值插入到列表头部。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key   key
     * @param value value
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    @Override
    public Long lPush(String key, String value) {
        return list().leftPush(key, value);
    }


    /**
     * Redis Lpush 命令将一个或多个值插入到列表头部。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key   key
     * @param value value
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    @Override
    public Long lPush(String key, Object value) {
        return list().leftPush(key, parseToString(value));
    }

    /**
     * Redis Lpush 命令将一个或多个值插入到列表头部。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key    key
     * @param values values
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    @Override
    public Long lPush(String key, String... values) {
        return list().leftPushAll(key, values);
    }

    /**
     * Redis Lpush 命令将一个或多个值插入到列表头部。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key    key
     * @param values values
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    @Override
    public Long lPush(String key, Object... values) {
        if (Objects.isNull(values)) {
            throw new IllegalArgumentException("lpush error");
        }
        return lPush(key, Arrays.asList(values));
    }


    /**
     * Redis Lpush 命令将一个或多个值插入到列表头部。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key    key
     * @param values values
     * @return 执行 LPUSH 命令后，列表的长度。
     */
    @Override
    public Long lPush(String key, List<Object> values) {
        List<String> elements = values.stream().map(this::parseToString).collect(Collectors.toList());
        return list().leftPushAll(key, elements);
    }

    //------------------------- zset ------------------------


    /**
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * zadd key score value
     *
     * @param key   key
     * @param value value
     * @param score score
     * @return
     */
    @Override
    public Boolean zAdd(String key, String value, double score) {
        return zset().add(key, value, score);
    }


    /**
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * zadd key score value
     *
     * @param key   key
     * @param value value
     * @param score score
     * @return
     */
    @Override
    public Boolean zAdd(String key, Object value, double score) {
        return zset().add(key, parseToString(value), score);
    }

    /**
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * zadd key score value
     *
     * @param key      key
     * @param valueSet valueSet
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    @Override
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> valueSet) {
        return zset().add(key, valueSet);
    }

    /**
     * Redis Zrem 命令用于移除有序集中的一个或多个成员，不存在的成员将被忽略。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key    key
     * @param values values
     * @return 被成功移除的成员的数量
     */
    @Override
    public Long zRem(String key, Object... values) {
        return zset().remove(key, values);
    }

    /**
     * Redis Zincrby 命令对有序集合中指定成员的分数加上增量 increment
     * 可以通过传递一个负数值 increment ，让分数减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
     * 当 key 不存在，或分数不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。
     * 当 key 不是有序集类型时，返回一个错误。
     * 分数值可以是整数值或双精度浮点数。
     *
     * @param key   key
     * @param value value
     * @param delta delta
     * @return member 成员的新分数值
     */
    @Override
    public Double zIncrBy(String key, String value, Double delta) {
        return zset().incrementScore(key, value, delta);
    }

    /**
     * Redis Zrank 返回有序集中指定成员的排名。
     *
     * @param key   key
     * @param value value(member)
     * @return 如果成员是有序集 key 的成员，返回 member 的排名。 如果成员不是有序集 key 的成员，返回NULL。
     */
    @Override
    public Long zRank(String key, Object value) {
        return zset().rank(key, value);
    }

    /**
     * Redis Zrevrank 命令返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。
     * 排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。
     * 使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名。
     *
     * @param key   key
     * @param value value
     * @return 排名，如果是1，表示排第二
     */
    @Override
    public Long zRevRank(String key, Object value) {
        return zset().reverseRank(key, value);
    }

    /**
     * Redis Zrange 返回有序集中，指定区间内的成员。
     * 其中成员的位置按分数值递增(从小到大)来排序。
     * 具有相同分数值的成员按字典序(lexicographical order )来排列。
     * 如果你需要成员按
     * 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public Set<String> zRange(String key, long start, long end) {
        return zset().range(key, start, end);
    }

    /**
     * Redis Zrange 返回有序集中，指定区间内的成员。
     * 其中成员的位置按分数值递增(从小到大)来排序。
     * 具有相同分数值的成员按字典序(lexicographical order )来排列。
     * 如果你需要成员按
     * 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 指定区间内，带有分数值的有序集成员的列表。
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start, long end) {
        return zset().rangeWithScores(key, start, end);
    }

    /**
     * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。
     * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。
     * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
     *
     * @param key key
     * @param min min
     * @param max max
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public Set<String> zRangeByScore(String key, double min, double max) {
        return zset().rangeByScore(key, min, max);
    }

    /**
     * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。带分页
     *
     * @param key    key
     * @param min    min
     * @param max    max
     * @param offset offset
     * @param count  需要的查询数量
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public Set<String> zRangeByScore(String key, double min, double max, long offset, long count) {
        return zset().rangeByScore(key, min, max, offset, count);
    }

    /**
     * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。
     * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。
     * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
     *
     * @param key key
     * @param min min
     * @param max max
     * @return 指定区间内，带有分数值的有序集成员的列表。
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max) {
        return zset().rangeByScoreWithScores(key, min, max);
    }

    /**
     * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。带分页
     *
     * @param key    key
     * @param min    min
     * @param max    max
     * @param offset offset
     * @param count  count
     * @return 指定区间内，带有分数值的有序集成员的列表。
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max,
                                                                          long offset, long count) {
        return zset().rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * Redis Zrevrange 命令返回有序集中，指定区间内的成员。
     * 其中成员的位置按分数值递减(从大到小)来排列。
     * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order)排列。
     * 除了成员按分数值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public Set<String> zRevRange(String key, long start, long end) {
        return zset().reverseRange(key, start, end);
    }

    /**
     * Redis Zrevrange 命令返回有序集中，指定区间内的成员。
     * 其中成员的位置按分数值递减(从大到小)来排列。
     * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order)排列。
     * 除了成员按分数值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScores(String key, long start, long end) {
        return zset().reverseRangeWithScores(key, start, end);
    }

    /**
     * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。
     * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
     * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
     *
     * @param key key
     * @param min min
     * @param max max
     * @return
     */
    @Override
    public Set<String> zRevRangeByScore(String key, double min, double max) {
        return zset().reverseRangeByScore(key, min, max);
    }

    /**
     * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。带分页
     *
     * @param key    key
     * @param min    min
     * @param max    max
     * @param offset offset
     * @param count  需要的记录条数
     * @return
     */
    @Override
    public Set<String> zRevRangeByScore(String key, double min, double max, long offset, long count) {
        return zset().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。- 带分页
     *
     * @param key    key
     * @param min    min
     * @param max    max
     * @param offset offset
     * @param count  需要的记录条数
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRevRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return zset().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * Redis Zcount 命令用于计算有序集合中指定分数区间的成员数量。
     *
     * @param key key
     * @param min min
     * @param max max
     * @return 分数值在 min 和 max 之间的成员的数量。
     */
    @Override
    public Long zCount(String key, double min, double max) {
        return zset().count(key, min, max);
    }

    /**
     * Redis Zcard 命令用于计算集合中元素的数量。
     *
     * @param key key
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
     */
    @Override
    public Long zCard(String key) {
        return zset().zCard(key);
    }

    /**
     * 集合中元素的数量。
     *
     * @param key
     * @return
     * @see this#zCard(String)
     */
    @Override
    public Long size(String key) {
        // 调用zcard方法
        return zCard(key);
    }


    /**
     * Redis Zscore 命令返回有序集中，成员的分数值。
     * 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
     *
     * @param key   key
     * @param value value
     * @return 成员的分数值
     */
    @Override
    public Double zScore(String key, Object value) {
        return zset().score(key, value);

    }

    /**
     * Redis Zremrangebylex 命令用于移除有序集合中给定的字典区间的所有成员。
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return 被成功移除的成员的数量
     */
    @Override
    public Long zRemRange(String key, long start, long end) {
        return zset().removeRange(key, start, end);
    }

    /**
     * 移除给定score区间的成员
     *
     * @param key key
     * @param min min
     * @param max max
     * @return 被成功移除的成员的数量
     */
    @Override
    public Long zRemRangeByScore(String key, double min, double max) {
        return zset().removeRangeByScore(key, min, max);
    }

    /**
     * Redis Zunionstore 命令计算给定的一个或多个有序集的并集，
     * 其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。
     * ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]
     * [WEIGHTS weight [weight ...]] 乘法系数默认为 1
     *
     * @param key      key
     * @param otherKey otherKey
     * @param destKey  destKey
     * @return 保存到 destination 的结果集的成员数量。
     */
    @Override
    public Long zUnionStore(String key, String otherKey, String destKey) {
        return zset().unionAndStore(key, otherKey, destKey);
    }


    /**
     * Redis Zunionstore 命令计算给定的一个或多个有序集的并集，
     * 其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。
     * ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]
     * [WEIGHTS weight [weight ...]] 乘法系数默认为 1
     *
     * @param key       key
     * @param otherKeys otherKeys
     * @param destKey   destKey
     * @return 保存到 destination 的结果集的成员数量。
     */
    @Override
    public Long zUnionStore(String key, Collection<String> otherKeys, String destKey) {
        return zset().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * Redis Zunionstore 命令计算给定的一个或多个有序集的并集，
     * 其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。
     * ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]
     *
     * @param key       key
     * @param otherKeys otherKeys
     * @param destKey   destKey
     * @return 保存到 destination 的结果集的成员数量。
     * @link https://www.runoob.com/redis/sorted-sets-zunionstore.html
     */
    @Override
    public Long zUnionStore(String key,
                            Collection<String> otherKeys,
                            String destKey,
                            RedisZSetCommands.Aggregate aggregate,
                            RedisZSetCommands.Weights weights) {
        return zset().unionAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    /**
     * Redis Zinterstore 用于计算多个有序集合的交集，并将结果存储在新的有序集合中。
     * Zinterstore 命令对于需要对有序集合进行集合运算（例如计算交集）时非常有用。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。
     *
     * @param key      key
     * @param otherKey otherKey
     * @param destKey  destKey
     * @return 保存到目标结果集的的成员数量。
     */
    @Override
    public Long zInterStore(String key, String otherKey, String destKey) {
        return zset().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * Redis Zinterstore 用于计算多个有序集合的交集，并将结果存储在新的有序集合中。
     * Zinterstore 命令对于需要对有序集合进行集合运算（例如计算交集）时非常有用。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。
     *
     * @param key       key
     * @param otherKeys otherKey
     * @param destKey   destKey
     * @return 保存到目标结果集的的成员数量。
     */
    @Override
    public Long zInterStore(String key, Collection<String> otherKeys, String destKey) {
        return zset().intersectAndStore(key, otherKeys, destKey);
    }


    /**
     * Redis Zinterstore 用于计算多个有序集合的交集，并将结果存储在新的有序集合中。
     * Zinterstore 命令对于需要对有序集合进行集合运算（例如计算交集）时非常有用。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。
     * <p>
     * WEIGHTS weight [weight ...]：指定每个有序集合的权重。默认权重为 1。
     * AGGREGATE sum|min|max：指定交集结果的聚合方式。默认是 sum（求和）。
     *
     * @param key       key
     * @param otherKeys otherKey
     * @param destKey   destKey
     * @param destKey   aggregate
     * @param destKey   weights
     * @return 保存到目标结果集的的成员数量。
     */
    @Override
    public Long zInterStore(String key, Collection<String> otherKeys,
                            String destKey,
                            RedisZSetCommands.Aggregate aggregate,
                            RedisZSetCommands.Weights weights) {
        return zset().intersectAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    /**
     * Redis Zscan 命令用于迭代有序集合中的元素（包括元素成员和元素分值）
     *
     * @param key         key
     * @param scanOptions scanOptions
     * @return 返回的每个元素都是一个有序集合元素，一个有序集合元素由一个成员（member）和一个分值（score）组成。
     */
    @Override
    public Cursor<ZSetOperations.TypedTuple<String>> zScan(String key, ScanOptions scanOptions) {
        return zset().scan(key, scanOptions);
    }

    /**
     * Redis Zscan 命令用于迭代有序集合中的元素（包括元素成员和元素分值）
     *
     * @param key      key
     * @param consumer consumer
     */
    @Override
    public void zScan(String key, Consumer<ZSetOperations.TypedTuple<String>> consumer) {
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .count(10000)
                .match("*")
                .build();
        try (Cursor<ZSetOperations.TypedTuple<String>> zScan = zScan(key, scanOptions)) {
            while (zScan.hasNext()) {
                consumer.accept(zScan.next());
            }
        } catch (IOException e) {
            log.error("[{Redis ZSCAN}]: >> zScan error~~", e);
        }
    }


    //------------------------- other ------------------------

    /**
     * 把列表中的对象转换成需要的类型
     *
     * @param valueList value的列表集合
     * @param clazz     需要的类型
     * @param <T>       需要的类型
     * @return List<T>
     * @see this#mget(Collection, Class)
     */
    private <T> List<T> parseToObject(Collection<String> valueList, Class<T> clazz) {
        return valueList.stream().map(v -> JSON.parseObject(v, clazz)).collect(Collectors.toList());
    }

    /**
     * String对象转换为具体类型对象
     *
     * @param value value
     * @param clazz clazz
     * @param <T>   <T>
     * @return T
     */
    private <T> T parseToObject(String value, Class<T> clazz) {
        return JSONObject.parseObject(value, clazz);
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
