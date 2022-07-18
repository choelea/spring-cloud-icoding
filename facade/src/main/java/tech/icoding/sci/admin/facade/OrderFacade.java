package tech.icoding.sci.admin.facade;

import java.lang.Long;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.admin.data.OrderData;
import tech.icoding.sci.admin.form.OrderForm;
import tech.icoding.sci.entity.OrderEntity;
import tech.icoding.sci.service.OrderService;

@Component
public class OrderFacade {
  private OrderService orderService;

  public OrderFacade(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * Get by ID
   */
  public OrderData get(Long id) {
    final OrderEntity orderEntity = orderService.find(id);
    final OrderData orderData = convert(orderEntity);
    return orderData;
  }

  /**
   * Find pageable data
   */
  public Page<OrderData> find(int page, int size) {
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<OrderEntity> entityPage = orderService.find(pageRequest);
    final List<OrderData> dataList = entityPage.getContent().stream().map(entity -> {
                return convert(entity);
            }).collect(Collectors.toList());
    final PageImpl<OrderData> dataPage = new PageImpl<OrderData>(dataList, entityPage.getPageable(), entityPage.getTotalElements());
    return dataPage;
  }

  /**
   * Create Entity and save to database
   */
  public OrderData create(OrderForm orderForm) {
    OrderEntity orderEntity = new OrderEntity();
    convert(orderForm,orderEntity);
    orderEntity = orderService.save(orderEntity);
    return convert(orderEntity);
  }

  /**
   * Update Entity  to database
   */
  public OrderData update(Long id, OrderForm orderForm) {
    OrderEntity orderEntity = orderService.find(id);
    convert(orderForm, orderEntity);
    orderEntity = orderService.update(orderEntity);
    return convert(orderEntity);
  }

  /**
   * Delete by ID
   */
  public void delete(Long id) {
    orderService.delete(id);
  }

  /**
   * Convert form to entity object
   */
  private void convert(OrderForm orderForm, OrderEntity orderEntity) {
    BeanUtils.copyProperties(orderForm, orderEntity);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  private OrderData convert(OrderEntity orderEntity) {
    final OrderData orderData = new OrderData();
    BeanUtils.copyProperties(orderEntity, orderData);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return orderData;
  }
}
