package io.github.alfonsokevin.core.limiter.strategy.key.impl;

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
 * @description 根据IP地址生成key
 * @since 2025-04-25 02:32
 * @author TangZhiKai
 **/
@Component
public class IpStrategy implements GeneratorKeyStrategy {
    private static final Logger log = LoggerFactory.getLogger(IpStrategy.class);
    public static final String X_FORWARDED_FOR = "x-forwarded-for";
    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String UNKNOWN = "unknown";
    public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";


    /**
     * 生成ip地址key的策略
     *
     * @param frequencyControl
     * @param joinPoint
     * @param method
     * @return
     */
    @Override
    public String getKey(FrequencyControl frequencyControl, ProceedingJoinPoint joinPoint,
                         Method method, HttpServletRequest request) {
        // TODO 考虑添加IPkey的逻辑
        log.debug("[{FrequencyControl}]: >> keyTypeStrategy:{}", frequencyControl.getKeyType().toString());
        return getClientIP(request);

    }

    @Override
    public KeyType getKeyType() {
        return KeyType.IP;
    }

    /**
     * HttpServletRequest中，尽可能准确地获取客户端真实IP
     *
     * @param request
     * @return
     */
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HTTP_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HTTP_X_FORWARDED_FOR);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
