package io.github.alfonsokevin.core.limiter.strategy.key.impl;

import io.github.alfonsokevin.core.limiter.enums.KeyType;
import io.github.alfonsokevin.core.limiter.model.FrequencyControl;
import io.github.alfonsokevin.core.limiter.strategy.key.GeneratorKeyStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @description: 根据IP地址生成key
 * @create: 2025-04-25 02:32
 * @author: TangZhiKai
 **/
@Component
public class IpStrategy implements GeneratorKeyStrategy {
    private static final Logger log = LoggerFactory.getLogger(IpStrategy.class);
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    public static final String UN_KNOWN = "unknown";


    /**
     * 生成ip地址key的策略
     * @param frequencyControl
     * @param joinPoint
     * @param method
     * @return
     */
    @Override
    public String getKey(FrequencyControl frequencyControl, ProceedingJoinPoint joinPoint,
                         Method method, HttpServletRequest request) {
        //直接用IP作为key吧
        //TODO 考虑添加IPkey的逻辑
        log.debug("[{FrequencyControl}]: >> keyTypeStrategy:{}", frequencyControl.getKeyType().toString());
        log.debug("[{FrequencyControl}]: >> ClientIP:{}", getClientIP(request));
        return getClientIP(request);

    }

    /**
     * HttpServletRequest中，尽可能准确地获取客户端真实IP
     * @param request
     * @return
     */
    private String getClientIP(HttpServletRequest request) {
        //优先从 X-Forwarded-For 头中获取
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.hasText(ip) && !UN_KNOWN.equalsIgnoreCase(ip)) {
            // X-Forwarded-For 可能是多个 IP，用逗号分隔，客户端真实 IP 通常在第一位
            return ip.split(",")[0];
        }
        // 2. 如果没有有效的 X-Forwarded-For，再尝试从 Proxy-Client-IP 头获取
        ip = request.getHeader(PROXY_CLIENT_IP);
        if (StringUtils.hasText(ip) && !UN_KNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        // 3. 再尝试从 WL-Proxy-Client-IP——WebLogic 特有代理头获取
        ip = request.getHeader(WL_PROXY_CLIENT_IP);
        if (StringUtils.hasText(ip) && !UN_KNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        // 4. 最后都没拿到，回退到 Servlet API 自带的方法
        return request.getRemoteAddr();
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.IP;
    }
}
