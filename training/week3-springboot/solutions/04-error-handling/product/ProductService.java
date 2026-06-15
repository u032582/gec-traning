package com.example.training.product;

import com.example.training.common.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 商品の業務処理（Service層）。課題2の list に加え、
 * findById を「見つからなければ例外を投げる」(404)に修正した版。
 */
@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /** IDで1件取得。見つからなければ ResourceNotFoundException を投げる（→ 共通ハンドラが404に変換）。 */
    public ProductResponse findById(Long productId) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("product not found: id=" + productId);
        }
        return ProductResponse.from(product);
    }

    /** 一覧取得。categoryId が null なら全件、指定があればそのカテゴリのみ。 */
    public List<ProductResponse> list(Long categoryId) {
        List<Product> products = (categoryId == null)
                ? productMapper.findAll()
                : productMapper.findByCategory(categoryId);
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }
}
