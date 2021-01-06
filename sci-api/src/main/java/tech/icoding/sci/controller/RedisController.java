package tech.icoding.sci.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.core.Result;
import tech.icoding.sci.service.redis.RedisService;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/7
 */
@RequestMapping("/redis")
@RestController
@Api("Redis 测试接口")
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
}
