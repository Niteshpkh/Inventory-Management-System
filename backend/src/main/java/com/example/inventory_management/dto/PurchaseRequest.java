package com.example.inventory_management.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequest {
    private String referenceNumber;
    private Long supplierId;
    private Long warehouseId;
    private List<PurchaseItemRequest> items;
}