package com.example.training.product;

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
