package tech.icoding.sci.service;

import java.lang.Long;
import org.springframework.stereotype.Service;
import tech.icoding.sci.entity.OrderEntity;
import tech.icoding.sci.repository.OrderRepository;

@Service
public class OrderService extends BaseService<OrderRepository, OrderEntity, Long> {
}
