package com.example.inventory_management.dto;

import com.example.inventory_management.enums.TransactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustmentRequest {
    private Long productId;
    private Long warehouseId;
    private com.example.inventory_management.enums.TransactionType transactionType;
    private Integer quantity;
    private String note;
}