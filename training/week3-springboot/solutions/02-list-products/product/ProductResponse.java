package com.example.training.product;

/**
 * 商品をAPIで返すときの形（DTO）。
 * 外には id / name / price だけを見せる（categoryId は返さない）。
 */
public record ProductResponse(
        Long id,
        String name,
        Integer price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice()
        );
    }
}
