package io.github.alfonsokevin.core.limiter.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

;

/**
 * @description: 速率限流器的构造器，寻找异常只反射一次，后续调用即可
 * @author: TangZhiKai
 **/
public class FrequencyControlBuilder {

    private static final Logger log = LoggerFactory.getLogger(FrequencyControlBuilder.class);

    /**
     * CACHE_MAP 静态缓存构造器
     * 便于找到具体异常字节码对象的构造器
     * 避免每次限流触发时都通过反射去获取构造器
     */
    private static final ConcurrentHashMap<Class<? extends RuntimeException>,
            Constructor<? extends RuntimeException>> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 构造器
     *
     * @param exceptionClass 注解自定义的枚举类
     * @param message        注解的消息
     * @return
     */
    public static RuntimeException build(Class<? extends RuntimeException> exceptionClass, String message) {
        // 如果存在则返回，不存在则构建
        try {
            Constructor<? extends RuntimeException> constructor = CACHE_MAP.computeIfAbsent(exceptionClass, c -> {
                try {
                    return c.getConstructor(String.class);
                } catch (NoSuchMethodException e) {
                    log.error("[{FrequencyControl}]: >> Abnormal initialization error of FrequencyControl",e);
                    throw new IllegalStateException("速率限流器异常初始化错误 ~~", e);
                }
            });
            return constructor.newInstance(message);
        } catch (Exception e) {
            log.error("[{FrequencyControl}]: >> Abnormal initialization error of FrequencyControl",e);
            throw new IllegalStateException("速率限流器异常初始化错误 ~~", e);
        }
    }

}
