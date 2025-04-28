package io.github.alfonsokevin.core.base.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @description: Redis构建key的工具类
 * @author: TangZhiKai
 **/
public class RedisKeyUtils {
    /**
     * 默认分隔符
     */
    public static final String DEFAULT_DELIMITER = ":";

    /**
     * 将需要构建的key按照要求的字符进行分隔
     *
     * @return 拼接好的字符串 a:b:c:d
     */
    public static String buildKey(String key) {
        return String.join(DEFAULT_DELIMITER, Collections.singletonList(key));
    }

    /**
     * 将需要构建的key按照要求的字符进行分隔
     *
     * @param keys 多个键
     * @return 拼接好的字符串 a:b:c:d
     */
    public static String buildKey(Collection<String> keys) {
        return String.join(DEFAULT_DELIMITER, keys);
    }

    /**
     * 将需要构建的key按照要求的字符进行分隔
     *
     * @param keys 多个键
     * @return 拼接好的字符串 a:b:c:d
     */
    public static String buildKey(String... keys) {
        return String.join(DEFAULT_DELIMITER, keys);
    }

    /**
     * 将需要构建的key按照要求的字符进行分隔
     *
     * @param delimiter 分隔符
     * @param keys      多个键
     * @return 拼接好的字符串 a:b:c:d
     */
    public static String buildKey(String delimiter, String... keys) {
        return String.join(delimiter, keys);
    }

    /**
     * 将需要构建的key按照要求的字符进行分隔
     *
     * @param delimiter 分隔符
     * @param keys      多个键
     * @return 拼接好的字符串 a:b:c:d
     */
    public static String buildKey(String delimiter, List<String> keys) {
        return String.join(delimiter, keys);
    }


}
