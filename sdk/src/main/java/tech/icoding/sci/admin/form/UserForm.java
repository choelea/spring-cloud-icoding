package tech.icoding.sci.admin.form;

import java.io.Serializable;
import java.lang.String;
import lombok.Data;
import tech.icoding.sci.sdk.common.UserType;

@Data
public class UserForm implements Serializable {
  private long serialVersionUID;

  private UserType userType;

  private String username;

  private String name;

  private String password;
}
