package tech.icoding.sci.service;

import java.lang.Long;
import org.springframework.stereotype.Service;
import tech.icoding.sci.entity.OrderItemEntity;
import tech.icoding.sci.repository.OrderItemRepository;

@Service
public class OrderItemService extends BaseService<OrderItemRepository, OrderItemEntity, Long> {
}
