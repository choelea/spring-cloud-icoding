package tech.icoding.sci.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.icoding.sci.core.BusinessException;
import tech.icoding.sci.core.Errors;
import tech.icoding.sci.core.Result;
import tech.icoding.sci.facade.RestFacade;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/7
 */
@RestController
@RequestMapping("/demo")
@Api(tags = "示例API")
public class DemoController {
    @Resource
    private RestFacade restFacade;

    @GetMapping("/exception")
    public Result get(){
        throw new BusinessException(Errors.ERROR_OTHER,"异常捕获示例");
    }

//    @PostMapping("/rest")
//    public Result test(@RequestBody UserForm userForm){
//        return restFacade.post("http://127.0.0.1:8080/sci-api/admin/users", userForm, Result.class);
//    }
}
