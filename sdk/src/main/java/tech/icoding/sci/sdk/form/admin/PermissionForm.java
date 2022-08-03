package tech.icoding.sci.sdk.form.admin;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import lombok.Data;

@Data
public class PermissionForm implements Serializable {
  private static final long serialVersionUID = 313613418802335932l;

  private String name;

  private String code;

  private String path;

  private String icon;

  private Integer sort;

  private Integer enable;

  private Integer type;

  private Long parent;
}
