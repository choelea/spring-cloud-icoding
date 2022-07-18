package tech.icoding.sci.admin.controller;

import java.lang.Long;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.icoding.sci.admin.data.OrderItemData;
import tech.icoding.sci.admin.facade.OrderItemFacade;
import tech.icoding.sci.admin.form.OrderItemForm;
import tech.icoding.sci.sdk.common.PageData;

@RestController
@RequestMapping("/orderitems")
public class OrderItemController {
  private OrderItemFacade orderItemFacade;

  public OrderItemController(OrderItemFacade orderItemFacade) {
    this.orderItemFacade = orderItemFacade;
  }

  /**
   * Get by ID
   */
  @GetMapping("/{id}")
  public OrderItemData get(@PathVariable final Long id) {
    final OrderItemData orderItemData = orderItemFacade.get(id);
    return orderItemData;
  }

  @GetMapping
  public PageData<OrderItemData> find(@RequestParam(defaultValue = "1") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    final Page<OrderItemData> page = orderItemFacade.find(pageNumber-1, pageSize);
    return new PageData<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent());
  }

  /**
   * Create
   */
  @PostMapping
  public OrderItemData create(@RequestBody final OrderItemForm orderItemForm) {
    return orderItemFacade.create(orderItemForm);
  }

  /**
   * Create
   */
  @PutMapping("/{id}")
  public OrderItemData update(@PathVariable final Long id,
      @RequestBody final OrderItemForm orderItemForm) {
    return orderItemFacade.update(id, orderItemForm);
  }

  /**
   * Delete by ID
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable final Long id) {
    orderItemFacade.delete(id);
  }
}
