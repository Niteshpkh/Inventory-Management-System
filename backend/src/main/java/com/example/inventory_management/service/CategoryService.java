package com.example.inventory_management.service;

import com.example.inventory_management.dto.CategoryRequest;
import com.example.inventory_management.dto.CategoryResponse;
import java.util.List;

public interface CategoryService   {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllActiveCategories();

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}