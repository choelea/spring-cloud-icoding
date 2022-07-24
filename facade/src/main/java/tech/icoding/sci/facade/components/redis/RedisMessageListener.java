package tech.icoding.sci.facade.components.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : Joe
 * @date : 2022/5/24
 */
@Slf4j
@Component
public class RedisMessageListener {
    /**
     *
     * @param message
     */
    public void onMessage(String message){
        log.info("收到消息："+message);
        //TODO 多实例同步更新, 比如同步更新内存缓存（如系统配置等）
    }
}
