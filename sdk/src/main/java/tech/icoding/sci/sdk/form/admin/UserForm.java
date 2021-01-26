package tech.icoding.sci.sdk.form.admin;

import lombok.Data;
import tech.icoding.sci.sdk.form.Jsonable;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
public class UserForm implements Jsonable {
    private String username;
    private String name;
    private String password;
}
