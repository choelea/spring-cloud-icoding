package tech.icoding.sci.facade;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.sdk.form.UserForm;
import tech.icoding.sci.service.UserService;
import tech.icoding.sci.entity.UserEntity;

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
        userEntity.setUsername(userForm.getUsername());
        userEntity.setName(userForm.getName());
        userEntity.setPassword(userForm.getName());
        final UserEntity u = userService.save(userEntity);
        return converter.convert(u);
    }

    public UserData update(Long id, UserForm userForm) {
        final UserEntity userEntity = userService.findById(id);
        userEntity.setName(userForm.getName());
        userEntity.setPassword(userForm.getPassword());
        userService.update(userEntity);
        return converter.convert(userEntity);
    }
}
