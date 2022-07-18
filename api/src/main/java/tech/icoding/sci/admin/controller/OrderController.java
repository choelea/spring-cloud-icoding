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
import tech.icoding.sci.admin.data.OrderData;
import tech.icoding.sci.admin.facade.OrderFacade;
import tech.icoding.sci.admin.form.OrderForm;
import tech.icoding.sci.sdk.common.PageData;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private OrderFacade orderFacade;

  public OrderController(OrderFacade orderFacade) {
    this.orderFacade = orderFacade;
  }

  /**
   * Get by ID
   */
  @GetMapping("/{id}")
  public OrderData get(@PathVariable final Long id) {
    final OrderData orderData = orderFacade.get(id);
    return orderData;
  }

  @GetMapping
  public PageData<OrderData> find(@RequestParam(defaultValue = "1") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    final Page<OrderData> page = orderFacade.find(pageNumber-1, pageSize);
    return new PageData<>(page.getTotalElements(), page.getNumber(), page.getSize(), page.getContent());
  }

  /**
   * Create
   */
  @PostMapping
  public OrderData create(@RequestBody final OrderForm orderForm) {
    return orderFacade.create(orderForm);
  }

  /**
   * Create
   */
  @PutMapping("/{id}")
  public OrderData update(@PathVariable final Long id, @RequestBody final OrderForm orderForm) {
    return orderFacade.update(id, orderForm);
  }

  /**
   * Delete by ID
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable final Long id) {
    orderFacade.delete(id);
  }
}
