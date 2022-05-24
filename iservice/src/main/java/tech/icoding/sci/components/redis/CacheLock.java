package tech.icoding.sci.components.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.icoding.sci.core.BusinessException;
import tech.icoding.sci.core.Errors;

import javax.annotation.Resource;
import java.time.Duration;


/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Slf4j
@Component("cacheLock")
public class CacheLock {
    /**
     * 缓存锁前缀
     */
    private static final String CACHE_LOCK = "CACHE_LOCK:CARD-CENTER";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 加锁
     *
     * @param key   被加锁键
     * @param value 当前时间+超时时间
     * @return boolean 是否被锁
     */
    public boolean lock(String key, String value, Duration timeout) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value, timeout)) {
            return true;
        }
        //当前锁时间
        String currentValue = redisTemplate.opsForValue().get(key);
        //锁过期
        if (!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValue =  redisTemplate.opsForValue().getAndSet(key, value);
            return !StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue);
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key   被解锁键
     * @param value 当前时间
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【cache分布式锁】解锁异常, key:{},value:{}", key, value);
            throw new BusinessException(Errors.ERROR_OTHER, "解锁异常");
        }
    }

    public static String lockKeyTemplate(String bizName, String lockKey) {
        return CACHE_LOCK + ":" + bizName + ":" + lockKey;
    }
}