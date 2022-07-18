package tech.icoding.sci.admin.facade;

import java.lang.Long;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import tech.icoding.sci.admin.data.OrderItemData;
import tech.icoding.sci.admin.form.OrderItemForm;
import tech.icoding.sci.entity.OrderItemEntity;
import tech.icoding.sci.service.OrderItemService;

@Component
public class OrderItemFacade {
  private OrderItemService orderItemService;

  public OrderItemFacade(OrderItemService orderItemService) {
    this.orderItemService = orderItemService;
  }

  /**
   * Get by ID
   */
  public OrderItemData get(Long id) {
    final OrderItemEntity orderItemEntity = orderItemService.find(id);
    final OrderItemData orderItemData = convert(orderItemEntity);
    return orderItemData;
  }

  /**
   * Find pageable data
   */
  public Page<OrderItemData> find(int page, int size) {
    final PageRequest pageRequest = PageRequest.of(page, size);
    final Page<OrderItemEntity> entityPage = orderItemService.find(pageRequest);
    final List<OrderItemData> dataList = entityPage.getContent().stream().map(entity -> {
                return convert(entity);
            }).collect(Collectors.toList());
    final PageImpl<OrderItemData> dataPage = new PageImpl<OrderItemData>(dataList, entityPage.getPageable(), entityPage.getTotalElements());
    return dataPage;
  }

  /**
   * Create Entity and save to database
   */
  public OrderItemData create(OrderItemForm orderItemForm) {
    OrderItemEntity orderItemEntity = new OrderItemEntity();
    convert(orderItemForm,orderItemEntity);
    orderItemEntity = orderItemService.save(orderItemEntity);
    return convert(orderItemEntity);
  }

  /**
   * Update Entity  to database
   */
  public OrderItemData update(Long id, OrderItemForm orderItemForm) {
    OrderItemEntity orderItemEntity = orderItemService.find(id);
    convert(orderItemForm, orderItemEntity);
    orderItemEntity = orderItemService.update(orderItemEntity);
    return convert(orderItemEntity);
  }

  /**
   * Delete by ID
   */
  public void delete(Long id) {
    orderItemService.delete(id);
  }

  /**
   * Convert form to entity object
   */
  private void convert(OrderItemForm orderItemForm, OrderItemEntity orderItemEntity) {
    BeanUtils.copyProperties(orderItemForm, orderItemEntity);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
  }

  /**
   * Convert entity to data object
   */
  private OrderItemData convert(OrderItemEntity orderItemEntity) {
    final OrderItemData orderItemData = new OrderItemData();
    BeanUtils.copyProperties(orderItemEntity, orderItemData);
    // TODO Override logic. (Copy properties is not the best solution, but an convenient one, for special logic, add below here )
    return orderItemData;
  }
}
