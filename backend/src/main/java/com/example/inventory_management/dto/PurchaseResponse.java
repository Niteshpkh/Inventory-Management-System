package com.example.inventory_management.dto;

import com.example.inventory_management.enums.PurchaseStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponse {
    private Long id;
    private String referenceNumber;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private String warehouseName;
    private com.example.inventory_management.enums.PurchaseStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime purchaseDate;
    private List<PurchaseItemResponse> items;
}