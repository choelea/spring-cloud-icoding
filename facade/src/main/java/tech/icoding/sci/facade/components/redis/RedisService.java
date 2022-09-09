package tech.icoding.sci.facade.components.redis;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis 服务
 * @author : Joe
 * @date : 2021/1/7
 */
@Component
public class RedisService {

    private StringRedisTemplate stringRedisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 添加缓存并设置过期时间
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public Boolean set(String key, String value, Duration timeout){
        return  stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }

    /**
     * 获取并设置过期时间
     * @param key
     * @param timeout
     * @return
     */
    public String get(String key, Duration timeout){
        return stringRedisTemplate.opsForValue().getAndExpire(key, timeout);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void convertAndSend(String channel, String message){
        stringRedisTemplate.convertAndSend(channel, message);
    }
}