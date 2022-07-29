package tech.icoding.sci.sdk.form.admin;

import java.io.Serializable;
import java.lang.String;
import lombok.Data;

@Data
public class RoleForm implements Serializable {
  private static final long serialVersionUID = 886238099689450704l;

  private String name;

  private String code;

  private String remark;
}
