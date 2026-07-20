package com.example.training.product;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.training.common.ResourceNotFoundException;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductResponse findById(Long productId) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("product not found: id=" + productId);
        }
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list(Long categoryId) {
        List<Product> products = (categoryId == null)
                ? productMapper.findAll()
                : productMapper.findByCategory(categoryId);

        List<ProductResponse> result = new ArrayList<>();
        for (Product product : products) {
            result.add(ProductResponse.from(product));
        }
        return result;
    }
}
