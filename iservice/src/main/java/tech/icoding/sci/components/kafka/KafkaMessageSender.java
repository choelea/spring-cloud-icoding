package tech.icoding.sci.components.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/2
 */
@Service
@Slf4j
public class KafkaMessageSender {
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message){
        send(topic, RandomStringUtils.random(9), message);
    }

    public void send(String topic,String key, String message){
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(topic,key, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Send to Topic={}, Message={} with offset={}", topic, message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send {} message={} due to: {}", topic, message, ex.getMessage());
                log.error(ex.getMessage(), ex);
            }
        });
    }
}