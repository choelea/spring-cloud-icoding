package tech.icoding.sci.facade.components.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
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

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void convertAndSend(String channel, String message){
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
