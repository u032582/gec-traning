package com.example.training.product;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品のDBアクセス窓口（MyBatis Mapper）。Repository層。
 * SQL本体は resources/mapper/ProductMapper.xml に書く。
 */
@Mapper
public interface ProductMapper {

    /** IDで商品を1件取得。見つからなければ null。 */
    Product findById(@Param("productId") Long productId);
}
