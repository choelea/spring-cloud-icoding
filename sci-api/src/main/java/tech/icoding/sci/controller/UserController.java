package tech.icoding.sci.controller;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.facade.UserFacade;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.form.UserForm;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@RestController
@RequestMapping("/users")
@Api(tags = "用户API")
public class UserController {
    @Resource
    private UserFacade userFacade;

    @GetMapping
    public Page<UserData> get(Pageable pageable) {
        return userFacade.findAll(pageable);
    }

    @GetMapping("/{id}")
    public UserData get(@PathVariable("id") Long id) {
        return userFacade.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userFacade.deleteById(id);
    }
    @PostMapping
    public UserData create(@RequestBody  UserForm userForm) {
        return userFacade.save(userForm);
    }

    @PutMapping("/{id}")
    public UserData update(@PathVariable("id") Long id, @RequestBody UserForm userForm) {
        return userFacade.update(id, userForm);
    }
}
