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
@Document(collection = "sale_items")
public class SaleItem {

    @Id
    private String id;

    private String saleId;

    private String productId;

    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}