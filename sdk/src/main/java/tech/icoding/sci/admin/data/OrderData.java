package tech.icoding.sci.admin.data;

import java.lang.Long;
import lombok.Data;
import tech.icoding.sci.sdk.common.BaseData;

@Data
public class OrderData extends BaseData<Long> {
  private static final long serialVersionUID = 975328759732552576l;

  private Long id;

  private Long amount;
}
