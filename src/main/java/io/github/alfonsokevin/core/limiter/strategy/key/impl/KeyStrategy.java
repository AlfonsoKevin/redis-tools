package io.github.alfonsokevin.core.limiter.strategy.key.impl;

import com.alibaba.fastjson.JSON;
import io.github.alfonsokevin.core.limiter.enums.KeyType;
import io.github.alfonsokevin.core.limiter.model.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @description: 自定义Key的生成策略
 * @create: 2025-04-22 14:54
 * @author: TangZhiKai
 **/
@Component
public class KeyStrategy implements GeneratorKeyStrategy {

    private static final Logger log = LoggerFactory.getLogger(KeyStrategy.class);

    /**
     * 直接使用默认的用户自定义的
     *
     * @param frequencyControl
     * @param method
     * @return
     */
    @Override
    public String getKey(FrequencyControl frequencyControl, ProceedingJoinPoint joinPoint,
                         Method method, HttpServletRequest request) {
        log.debug("[{FrequencyControl}]: >> keyTypeStrategy:{}", frequencyControl.getKeyType().toString());
        return frequencyControl.getKey();
    }
    /**
     * 获取key类型
     * @return
     */
    @Override
    public KeyType getKeyType() {
        return KeyType.KEY;
    }
}
