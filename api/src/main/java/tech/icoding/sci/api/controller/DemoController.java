package tech.icoding.sci.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.core.components.kafka.KafkaMessageSender;
import tech.icoding.sci.core.components.redis.RedisService;
import tech.icoding.sci.sdk.common.BusinessException;
import tech.icoding.sci.sdk.common.Constants;
import tech.icoding.sci.sdk.common.Errors;
import tech.icoding.sci.sdk.common.Result;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/7
 */
@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @Resource
    private RedisService redisService;

    @Resource
    private KafkaMessageSender kafkaMessageSender;
    @GetMapping("/exception")
    public Result get(){
        throw new BusinessException(Errors.ERROR_OTHER,"异常捕获示例");
    }


    @PostMapping("/kafka")
    public Result kafka(@RequestParam String topic, @RequestParam String message){
        kafkaMessageSender.send(topic, message);
        return Result.success();
    }


    @GetMapping("/{key}")
    public Result get(@PathVariable String key){
        return Result.success(redisService.get(key));
    }
    @PostMapping("/{key}")
    public Result set(@PathVariable String key, @RequestBody String value){
        redisService.set(key, value);
        return Result.success();
    }
    @PostMapping("/message")
    public Result message(@RequestParam String msg){
        redisService.convertAndSend(Constants.RedisChannel.CACHE_REFRESH, msg);
        log.info("发送Redis消息");
        return Result.success();
    }

}