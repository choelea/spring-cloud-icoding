package tech.icoding.sci.sdk.data;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import lombok.Data;
import tech.icoding.sci.sdk.common.BaseData;

@Data
public class PermissionData extends BaseData<Long> {
  private static final long serialVersionUID = 751388484626764643l;

  private Long id;

  private String name;

  private String code;

  private String path;

  private String icon;

  private Integer sort;

  private Integer enable;

  private Integer type;
}
