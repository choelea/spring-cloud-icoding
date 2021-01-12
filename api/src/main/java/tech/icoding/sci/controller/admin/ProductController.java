package tech.icoding.sci.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import tech.icoding.sci.facade.admin.ProductFacade;
import tech.icoding.sci.sdk.data.ProductData;
import tech.icoding.sci.sdk.form.ProductForm;

import javax.annotation.Resource;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@RestController
@RequestMapping("/Products")
@Api(tags = "Product管理API")
public class ProductController {
    @Resource
    private ProductFacade productFacade;

    @GetMapping
    public Page<ProductData> get(@RequestParam int page, @RequestParam int size) {
        return productFacade.findAll(PageRequest.of(page-1, size));
    }

    @GetMapping("/{id}")
    public ProductData get(@PathVariable("id") Long id) {
        return productFacade.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        productFacade.deleteById(id);
    }
    @PostMapping
    public ProductData create(@RequestBody ProductForm productForm) {
        return productFacade.save(productForm);
    }

    @PutMapping("/{id}")
    public ProductData update(@PathVariable("id") Long id, @RequestBody ProductForm productForm) {
        return productFacade.update(id, productForm);
    }
}

