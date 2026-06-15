package com.example.training.product;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品のDBアクセス窓口（MyBatis Mapper）。
 * 課題1の findById に、一覧取得（やり方A: 2メソッド）を追加した版。
 */
@Mapper
public interface ProductMapper {

    /** IDで商品を1件取得。見つからなければ null。 */
    Product findById(@Param("productId") Long productId);

    /** 全商品を取得（id昇順）。 */
    List<Product> findAll();

    /** 指定カテゴリの商品を取得（id昇順）。 */
    List<Product> findByCategory(@Param("categoryId") Long categoryId);
}
