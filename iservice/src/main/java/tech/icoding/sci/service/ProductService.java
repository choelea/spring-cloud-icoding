package tech.icoding.sci.service;

import org.springframework.stereotype.Service;
import tech.icoding.sci.entity.ProductEntity;
import tech.icoding.sci.repository.ProductRepository;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Service
public class ProductService extends BaseService<ProductRepository, ProductEntity, Long>{
}
