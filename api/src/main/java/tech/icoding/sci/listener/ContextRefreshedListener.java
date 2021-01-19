package tech.icoding.sci.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 此处可以添加用于 spring 上线文加载后处理的逻辑。
 */
@Component
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent>{


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		log.info("收到ContextRefreshedEvent事件------------");
	}

	/**
	 *
	 * @param message
	 */
	public void onMessage(String message){
		log.info("收到消息："+message);
		//TODO 多实例同步更新, 比如同步更新内存缓存（如系统配置等）
	}

}