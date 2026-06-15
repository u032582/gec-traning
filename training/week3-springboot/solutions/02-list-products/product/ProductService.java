package com.example.training.product;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 商品の業務処理（Service層）。
 * 一覧取得を追加。categoryId の有無で呼ぶMapperメソッドを分岐する（やり方A）。
 */
@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /** IDで1件取得。見つからなければ null。 */
    public ProductResponse findById(Long productId) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            return null;
        }
        return ProductResponse.from(product);
    }

    /**
     * 一覧取得。categoryId が null なら全件、指定があればそのカテゴリのみ。
     * 取得した List<Product> を List<ProductResponse> に詰め替えて返す。
     */
    public List<ProductResponse> list(Long categoryId) {
        List<Product> products = (categoryId == null)
                ? productMapper.findAll()
                : productMapper.findByCategory(categoryId);
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }
}
