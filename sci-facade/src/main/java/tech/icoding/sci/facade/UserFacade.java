package tech.icoding.sci.facade;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.form.UserForm;
import tech.icoding.sci.service.UserService;
import tech.icoding.sci.service.entity.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Service
public class UserFacade {

    @Resource
    private UserService userService;

    @Resource
    private Converter<User, UserData> converter;

    public UserData get(Long id){
        final User user = userService.findById(id);
        return converter.convert(user);
    }

    public Page<UserData> findAll(Pageable pageable) {
        final Page<User> page = userService.findAll(pageable);
        List<UserData> list = new ArrayList<>(page.getContent().size());
        for (User user:page.getContent()) {
            list.add(converter.convert(user));
        }
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements() );
    }

    public void deleteById(Long id) {
        userService.deleteById(id);
    }

    public UserData save(UserForm userForm) {
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setName(userForm.getName());
        user.setPassword(userForm.getName());
        final User u = userService.save(user);
        return converter.convert(u);
    }

    public UserData update(Long id, UserForm userForm) {
        final User user = userService.findById(id);
        user.setName(userForm.getName());
        user.setPassword(userForm.getPassword());
        userService.update(user);
        return converter.convert(user);
    }
}
