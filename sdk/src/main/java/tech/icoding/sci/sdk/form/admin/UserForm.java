package tech.icoding.sci.sdk.form.admin;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Set;
import lombok.Data;

@Data
public class UserForm implements Serializable {
  private static final long serialVersionUID = 279279174370601792l;

  private String userName;

  private String nickName;

  private String password;

  private String salt;

  private Integer enableFlag;

  private String phone;

  private String email;

  private String remark;

  private Set<Long> roles;
}