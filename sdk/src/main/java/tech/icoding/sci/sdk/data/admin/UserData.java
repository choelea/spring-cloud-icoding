package tech.icoding.sci.sdk.data.admin;

import lombok.Data;
import tech.icoding.sci.sdk.common.UserType;

import java.io.Serializable;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
public class UserData implements Serializable {
    private static final long serialVersionUID = -3935785670116804504L;
    private Long id;
    private String createDate;
    private String lastModifiedDate;
    private String username;
    private String name;
    private UserType userType;
}
