package tech.icoding.sci.facade.converter.todto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.service.entity.User;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Component
public class UserDtoConverter implements Converter<User, UserData> {
    @Override
    public UserData convert(User user) {
        UserData userData = new UserData();
        userData.setUsername(user.getUsername());
        userData.setName(user.getName());
        return userData;
    }
}
