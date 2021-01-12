package tech.icoding.sci.facade.admin;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tech.icoding.sci.entity.ProductEntity;
import tech.icoding.sci.sdk.data.ProductData;
import tech.icoding.sci.sdk.form.ProductForm;
import tech.icoding.sci.service.ProductService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Component
public class ProductFacade {
    @Resource
    private ProductService productService;

    @Resource
    private Converter<ProductEntity, ProductData> converter;

    public ProductData get(Long id){
        final ProductEntity productEntity = productService.findById(id);
        return converter.convert(productEntity);
    }

    public Page<ProductData> findAll(Pageable pageable) {
        final Page<ProductEntity> page = productService.findAll(pageable);
        List<ProductData> list = new ArrayList<>(page.getContent().size());
        for (ProductEntity productEntity :page.getContent()) {
            list.add(converter.convert(productEntity));
        }
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements() );
    }

    public void deleteById(Long id) {
        productService.deleteById(id);
    }

    public ProductData save(ProductForm ProductForm) {
        ProductEntity productEntity = new ProductEntity();
        //TODO 将Form的值设置到Entity
        final ProductEntity u = productService.save(productEntity);
        return converter.convert(u);
    }

    public ProductData update(Long id, ProductForm productForm) {
        final ProductEntity productEntity = productService.findById(id);
        //TODO 将 form的值设置到entity
        productService.update(productEntity);
        return converter.convert(productEntity);
    }
}

