package com.example.training.product;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品APIの窓口（Controller層）。一覧取得を追加した版。
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** GET /api/products/{id} … 商品を1件取得。 */
    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable("id") Long id) {
        return productService.findById(id);
    }

    /**
     * GET /api/products?categoryId=... … 商品一覧。
     * categoryId は任意。省略時は全件。
     */
    @GetMapping
    public List<ProductResponse> listProducts(
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        return productService.list(categoryId);
    }
}
