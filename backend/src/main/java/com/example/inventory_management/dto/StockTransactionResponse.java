package com.example.inventory_management.dto;

import com.example.inventory_management.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransactionResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private Long warehouseId;
    private String warehouseName;
    private com.example.inventory_management.enums.TransactionType transactionType;
    private Integer quantity;
    private Integer balanceAfter;
    private String referenceNumber;
    private String note;
    private LocalDateTime createdAt;
}