package tech.icoding.sci.sdk.data;

import java.lang.Long;
import java.lang.String;
import lombok.Data;
import tech.icoding.sci.sdk.common.BaseData;

@Data
public class RoleData extends BaseData<Long> {
  private static final long serialVersionUID = 554573724731996870l;

  private Long id;

  private String name;

  private String code;

  private String remark;
}
