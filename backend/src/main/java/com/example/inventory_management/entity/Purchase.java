package com.example.inventory_management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "purchases")
public class Purchase {

    @Id
    private String id;

    private String purchaseNumber;

    private String supplierId;

    private String wareHouseId;

    private LocalDateTime purchaseDate;

    private BigDecimal totalAmount;

    private String status;
}