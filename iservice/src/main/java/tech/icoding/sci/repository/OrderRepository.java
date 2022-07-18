package tech.icoding.sci.repository;

import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.icoding.sci.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaSpecificationExecutor<Long>, JpaRepository<OrderEntity, Long> {
}
