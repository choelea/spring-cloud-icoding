package tech.icoding.sci.admin.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderForm implements Serializable {
  private static final long serialVersionUID = 944184979165581184l;

  private Long amount;

  private List<Long> items;
}
