package tech.icoding.sci.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.core.Result;
import tech.icoding.sci.facade.RestFacade;
import tech.icoding.sci.sdk.common.Constants;
import tech.icoding.sci.service.redis.RedisService;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/7
 */
@RequestMapping("/redis")
@RestController("Redis 测试接口")
@Api(tags = "Redis 测试接口")
@Slf4j
public class RedisController {
    @Resource
    private RedisService redisService;

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
