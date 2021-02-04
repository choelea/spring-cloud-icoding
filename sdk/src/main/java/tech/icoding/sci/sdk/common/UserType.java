package tech.icoding.sci.sdk.common;

/**
 * @author : Joe
 * @date : 2021/2/4
 */
public enum UserType implements IEnum{
    ADMIN(1),
    BUYER(2);

    private Integer code;

    UserType(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
