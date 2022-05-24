package tech.icoding.sci.admin.data;

import java.lang.Long;
import java.lang.String;
import lombok.Data;
import tech.icoding.sci.sdk.common.BaseData;
import tech.icoding.sci.sdk.common.UserType;

@Data
public class UserData extends BaseData<Long> {
  private long serialVersionUID;

  private Long id;

  private UserType userType;

  private String username;

  private String name;

  private String password;
}
