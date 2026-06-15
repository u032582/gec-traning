package com.example.training.product;

/**
 * products テーブル1行ぶんを表すエンティティ。
 * 列 product_id / category_id は map-underscore-to-camel-case により
 * productId / categoryId に自動対応する。
 */
public class Product {

    private Long productId;
    private String name;
    private Integer price;
    private Long categoryId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
