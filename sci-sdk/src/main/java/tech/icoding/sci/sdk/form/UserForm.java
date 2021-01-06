package tech.icoding.sci.sdk.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Data
@ApiModel(value = "用户表单")
public class UserForm {
    private String username;
    private String name;
    private String password;
}
