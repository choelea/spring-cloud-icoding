package tech.icoding.sci.facade.admin;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tech.icoding.sci.entity.UserEntity;
import tech.icoding.sci.sdk.common.UserType;
import tech.icoding.sci.sdk.data.admin.UserData;
import tech.icoding.sci.sdk.form.admin.UserForm;
import tech.icoding.sci.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Component
public class UserFacade {
    @Resource
    private UserService userService;

    @Resource
    private Converter<UserEntity, UserData> converter;

    public UserData get(Long id){
        final UserEntity userEntity = userService.findById(id);
        return converter.convert(userEntity);
    }

    public Page<UserData> findAll(Pageable pageable) {
        final Page<UserEntity> page = userService.findAll(pageable);
        List<UserData> list = new ArrayList<>(page.getContent().size());
        for (UserEntity userEntity :page.getContent()) {
            list.add(converter.convert(userEntity));
        }
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements() );
    }

    public void deleteById(Long id) {
        userService.deleteById(id);
    }

    public UserData save(UserForm userForm) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userForm.getName());
        userEntity.setPassword(userForm.getPassword());
        userEntity.setUsername(userForm.getUsername());
        userEntity.setUserType(UserType.BUYER);
        final UserEntity u = userService.save(userEntity);
        return converter.convert(u);
    }

    public UserData update(Long id, UserForm userForm) {
        final UserEntity userEntity = userService.findById(id);
        userEntity.setId(id);
        userEntity.setUsername(userForm.getUsername());
        userEntity.setName(userForm.getName());
        userService.update(userEntity);
        return converter.convert(userEntity);
    }
}

