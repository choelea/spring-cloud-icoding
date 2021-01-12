package tech.icoding.sci.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.icoding.sci.entity.ProductEntity;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {
}
