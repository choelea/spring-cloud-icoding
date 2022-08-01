package tech.icoding.sci.sdk.data;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import lombok.Data;
import tech.icoding.sci.sdk.common.BaseData;

@Data
public class UserData extends BaseData<Long> {
  private static final long serialVersionUID = 112655606980547897l;

  private Long id;

  private String userName;

  private String nickName;

  private String password;

  private String salt;

  private Integer enableFlag;

  private String phone;

  private String email;

  private String remark;
}
