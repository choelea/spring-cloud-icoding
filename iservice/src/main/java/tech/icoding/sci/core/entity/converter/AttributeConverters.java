package tech.icoding.sci.core.entity.converter;

import org.springframework.stereotype.Component;
import tech.icoding.sci.sdk.common.UserType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Component
public class AttributeConverters{

    @Converter(autoApply = true)
    public static class UserTypeConverter implements AttributeConverter<UserType, Integer> {

        @Override
        public Integer convertToDatabaseColumn(UserType attribute) {
            return attribute.getCode();
        }

        @Override
        public UserType convertToEntityAttribute(Integer code) {
            if (code == null) {
                return null;
            }

            return Stream.of(UserType.values())
                    .filter(c -> c.getCode().equals(code))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}