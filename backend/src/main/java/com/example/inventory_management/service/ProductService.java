package com.example.inventory_management.service;

import com.example.inventory_management.dto.ProductRequest;
import com.example.inventory_management.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    ProductResponse getProductBySku(String sku);

    List<ProductResponse> getAllActiveProducts();

    List<ProductResponse> getProductsByCategory(Long categoryId);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}