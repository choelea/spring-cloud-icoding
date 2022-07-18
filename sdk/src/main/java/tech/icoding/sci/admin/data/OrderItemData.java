package tech.icoding.sci.admin.data;

import lombok.Data;
import tech.icoding.sci.sdk.common.BaseData;

@Data
public class OrderItemData extends BaseData<Long> {
  private static final long serialVersionUID = 439141402347727296l;

  private Long id;

  private Integer quantity;

  private Long unitPrice;

  private Long amount;
}
