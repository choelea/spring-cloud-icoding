package tech.icoding.sci.sdk.data;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Data
@ApiModel(value = "用户返回DTO")
public class UserData {
    private String username;
    private String name;
}
