package io.github.alfonsokevin.core.cache.job;

import io.github.alfonsokevin.core.base.constants.RTConstants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @description 定时任务删除队列中的Key
 * @since 2025-04-27 17:58
 * @author TangZhiKai
 **/
@Component
@RequiredArgsConstructor
public class CacheEvictJob {

    // @Value("${redis.tools.schedular.evictKeyCron}")
    // private String evictKeyCron;
    private final RedissonClient redissonClient;

    private final Logger log = LoggerFactory.getLogger(CacheEvictJob.class);
    /**
     * 时间格式
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 定时任务获取队列中的信息，删除key
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void cacheEvictTask() {
        // 开始
        log.info("[{RedisCacheEvict}]: >> " +
                "The start time of the scheduled task. {}", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        RBlockingQueue<Object> queue = redissonClient.getBlockingQueue(RTConstants.REDIS_CACHE_EVICT_QUEUE);
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(queue);
        // 从队列中取出元素消费
        String waitDelKey = null;
        try {
            waitDelKey = Optional.ofNullable(queue.poll())
                    .map(String::valueOf)
                    .orElse(null);
            if (Objects.isNull(waitDelKey)) {
                // 不执行任何消费逻辑
                return;
            }
            // 获取key，延时双删
            RKeys rKeys = redissonClient.getKeys();
            long delete = rKeys.delete(waitDelKey);
            log.info("remove waitDel key in Queue : {} ", waitDelKey);
            // 说明仍然存在
            // TODO
            // if (delete == 1) {
            //     // 如果删除失败，重新加入到队列中
            //     delayedQueue.offer(waitDelKey, 0, TimeUnit.SECONDS);
            // }
        } catch (Exception e) {
            log.warn("[{RedisCacheEvict}]: >> " +
                    "Acquiring the evict keys fail ! {}. Re-consumption ", LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
            // 如果抛出异常，重新加入到队列中
            delayedQueue.offer(waitDelKey, 0, TimeUnit.SECONDS);
        }
        log.info("The end time of the scheduled task. {}", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
    }
}
