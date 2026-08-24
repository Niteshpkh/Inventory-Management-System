package com.example.inventory_management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String sku;

    private String name;

    private String description;

    private String categoryId;

    private BigDecimal costPrice;

    private BigDecimal sellingPrice;

    private int reorderLevel;

    private boolean isActive;
}