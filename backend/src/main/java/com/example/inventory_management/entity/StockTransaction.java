package com.example.inventory_management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.management.relation.TransactionType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_transactions")
public class StockTransaction {

    @Id
    private String id;

    private String productId;

    private String WareHouseId;

    private int quantity;

    private TransactionType transactionType;

    private String referenceId;

    private String reason;

    private LocalDateTime createdAt;
}