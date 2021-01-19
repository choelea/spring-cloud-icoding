package tech.icoding.sci.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import tech.icoding.sci.listener.ContextRefreshedListener;
import tech.icoding.sci.sdk.common.Constants;

/**
 * @author : Joe
 * @date : 2021/1/19
 */
@Configuration
public class RedisConfig {
    /**
     * Redis消息监听容器，绑定消息监听适配器
     * @param connectionFactory
     * @param listenerAdapter 适配器1
     * @return
     */
    @Bean(name = "listenerContainer")
    RedisMessageListenerContainer listenerContainer(RedisConnectionFactory connectionFactory,
                                                    @Qualifier("listenerAdapter") MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //监听容器可添加多个适配器，监听多个频道
        container.addMessageListener(listenerAdapter, new PatternTopic(Constants.RedisChannel.CACHE_REFRESH));
        return container;
    }

    /**
     * 消息监听适配器，绑定消息接收器
     * @param contextRefreshedListener 消息接收器
     * @return
     */
    @Bean(name = "listenerAdapter")
    MessageListenerAdapter contextRefreshedListener(ContextRefreshedListener contextRefreshedListener) {
        //利用反射技术，调用消息处理方法
        return new MessageListenerAdapter(contextRefreshedListener, "onMessage");
    }

}
