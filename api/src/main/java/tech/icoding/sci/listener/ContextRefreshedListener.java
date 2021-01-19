package tech.icoding.sci.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
 
@Component
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent>{


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("开始初始化数据------------");
	}

	public void onMessage(String message){
		System.out.println("收到消息："+message);
	}

}