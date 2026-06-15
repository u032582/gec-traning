package com.example.training.product;

import org.springframework.stereotype.Service;

/**
 * 商品の業務処理（Service層）。
 * Mapperで取得した Product を ProductResponse に詰め替えて返す。
 */
@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /** IDで1件取得。見つからなければ null（404対応は課題4で追加する）。 */
    public ProductResponse findById(Long productId) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            return null;
        }
        return ProductResponse.from(product);
    }
}
