package tech.icoding.sci.converter.todata;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tech.icoding.sci.sdk.common.Constants;
import tech.icoding.sci.entity.UserEntity;
import tech.icoding.sci.sdk.data.admin.UserData;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Component
public class UserDataConverter implements Converter<UserEntity, UserData> {
    @Override
    public UserData convert(UserEntity source) {
        UserData target = new UserData();
        target.setId(source.getId());
        target.setCreateDate(source.getCreatedDate().format(Constants.DATE_TIME_FORMATTER));
        target.setLastModifiedDate(source.getLastModifiedDate().format(Constants.DATE_TIME_FORMATTER));
        target.setName(source.getName());
        target.setUsername(source.getUsername());
        return target;
    }
}

