package tech.icoding.sci.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.core.Result;
import tech.icoding.sci.facade.admin.UserFacade;
import tech.icoding.sci.sdk.data.admin.UserData;
import tech.icoding.sci.sdk.form.admin.UserForm;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@RestController
@RequestMapping("/admin/users")
@Api(tags = "User管理API")
public class UserController {
    @Resource
    private UserFacade userFacade;

    @GetMapping
    public Page<UserData> get(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return userFacade.findAll(PageRequest.of(page-1, size));
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable("id") Long id) {
        UserData userData = userFacade.get(id);
        return Result.success(userData);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        userFacade.deleteById(id);
        return Result.success();
    }
    @PostMapping
    public Result create(@RequestBody UserForm userForm) {
        UserData userData = userFacade.save(userForm);
        return Result.success(userData);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id, @RequestBody UserForm userForm) {
        UserData userData = userFacade.update(id, userForm);
        return Result.success(userData);
    }
}

