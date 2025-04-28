package io.github.alfonsokevin.core.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 自动配置类
 * @author: TangZhiKai
 **/
@Configuration
@ConditionalOnMissingBean(RedissonClient.class)
@EnableConfigurationProperties(RedisToolsProperties.class)
public class RedisToolsAutoConfiguration {

    @Bean(value = "redissonToolsClient")
    public RedissonClient redissonClient(RedisToolsProperties redisToolsProperties) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + redisToolsProperties.getHost() + ":" + redisToolsProperties.getPort());
        if (StringUtils.isNotBlank(redisToolsProperties.getPassword())) {
            singleServerConfig.setPassword(redisToolsProperties.getPassword());
        }
        singleServerConfig.setKeepAlive(true);
        singleServerConfig.setDatabase(redisToolsProperties.getDatabase());
        singleServerConfig.setClientName(redisToolsProperties.getServerName());
        return Redisson.create(config);
    }
}
