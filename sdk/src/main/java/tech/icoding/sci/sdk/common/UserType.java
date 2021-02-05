package tech.icoding.sci.sdk.common;

/**
 * @author : Joe
 * @date : 2021/2/4
 */
public enum UserType implements IEnum{
    ADMIN(1,"管理员"),
    BUYER(2, "买家");

    private Integer code;
    private String desc;

    UserType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }


}
