package tech.icoding.sci.converter.todto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tech.icoding.sci.sdk.data.UserData;
import tech.icoding.sci.entity.UserEntity;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Component
public class UserDtoConverter implements Converter<UserEntity, UserData> {
    @Override
    public UserData convert(UserEntity userEntity) {
        UserData userData = new UserData();
        userData.setUsername(userEntity.getUsername());
        userData.setName(userEntity.getName());
        return userData;
    }
}
