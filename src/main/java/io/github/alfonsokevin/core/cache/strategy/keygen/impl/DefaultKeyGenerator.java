package io.github.alfonsokevin.core.cache.strategy.keygen.impl;

import com.alibaba.fastjson2.JSON;
import io.github.alfonsokevin.core.cache.model.RedisCacheable;
import io.github.alfonsokevin.core.cache.model.enums.KeyType;
import io.github.alfonsokevin.core.cache.strategy.keygen.CacheableKeyGenerator;
import io.github.alfonsokevin.core.base.utils.SpElUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: 默认的生成策略，拼接key
 * @create: 2025-04-23 18:02
 * @author: TangZhiKai
 **/
@Component
public class DefaultKeyGenerator implements CacheableKeyGenerator {
    private static final DefaultParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 将方法中的全部参数拼接作为key
     *
     * @param cacheable 注解实体类
     * @param joinPoint 切入点
     * @param method    增强方法信息
     * @return
     */
    @Override
    public String getKey(RedisCacheable cacheable, ProceedingJoinPoint joinPoint, Method method) {
        Object[] args = joinPoint.getArgs();
        String originalKey = cacheable.getKey();
        // 如果没有设置key，那么默认就是前缀名 + 参数拼接 。如果有设置key，但是没设置前缀，返回key，否则返回
        return isNotBlank(originalKey) ? (cacheable.getAutoPrefixKey() ?
                String.format("%s:%s", SpElUtils.getMethodPrefix(method), originalKey)
                : originalKey)
                : getDefaultKey(method, args);

    }

    /**
     * 生成默认的key的策略
     * TODO 可以用StringBuilder吗？
     * 前缀名 + 参数拼接
     *
     * @param method
     * @param args
     * @return
     */
    private String getDefaultKey(Method method, Object[] args) {
        String[] params = NAME_DISCOVERER.getParameterNames(method);
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("NAME_DISCOVER parse is failed ~~");
        }
        // String#abc:Long#123
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            if (i != params.length - 1 && i != 0) {
                stringBuffer.append(":");
            }
            // 可以优化
            stringBuffer.append(params[i]).append("#").append(parseToStr(args[i]));
        }
        return stringBuffer.toString();
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.DEFAULT;
    }

    /**
     * 判断传递的字符串是否为空
     * TODO 看看有没有Commonlang包
     *
     * @param str
     * @return
     */
    private static boolean isNotBlank(String str) {
        return str != null && str.length() != 0;
    }

    /**
     * 解析字符串
     *
     * @param obj 原始对象
     * @return 字符串对象
     */
    private String parseToStr(Object obj) {
        if (obj instanceof String) {
            // 如果参数是String，并且长度大于3了，那么只输出前3位
            return (String) obj;
        }
        if (obj instanceof Integer || obj instanceof Long || obj instanceof Float || obj instanceof Double) {
            return String.valueOf(obj);
        }
        return JSON.toJSONString(obj);
    }
}
