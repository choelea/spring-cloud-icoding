package tech.icoding.sci.facade.converter.todto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tech.icoding.sci.entity.ProductEntity;
import tech.icoding.sci.sdk.data.ProductData;

/**
 * @author : Joe
 * @date : 2021/1/6
 */
@Component
public class ProductDataConverter implements Converter<ProductEntity, ProductData> {
    @Override
    public ProductData convert(ProductEntity source) {
        ProductData target = new ProductData();

        // TODO 设置 data 内容
        return target;
    }
}

