package tech.icoding.sci.sdk.form;

import java.io.Serializable;
import java.lang.String;
import lombok.Data;

@Data
public class RoleForm implements Serializable {
  private static final long serialVersionUID = 953819028459653650l;

  private String name;

  private String code;

  private String remark;
}
