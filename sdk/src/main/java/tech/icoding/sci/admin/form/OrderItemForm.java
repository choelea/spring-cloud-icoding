package tech.icoding.sci.admin.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemForm implements Serializable {
  private static final long serialVersionUID = 177326743903550432l;

  private String productName;

  private Integer quantity;

  private Long unitPrice;

  private Long amount;

  private Long order;
}
