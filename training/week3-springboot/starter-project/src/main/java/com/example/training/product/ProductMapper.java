package com.example.training.product;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {
    Product findById(@Param("productId") Long productId);
    List<Product> findAll();
    List<Product> findByCategory(@Param("categoryId") Long categoryId);
}