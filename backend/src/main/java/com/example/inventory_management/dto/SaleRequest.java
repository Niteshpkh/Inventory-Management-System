package com.example.inventory_management.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleRequest {
    private String invoiceNumber;
    private Long customerId;
    private Long warehouseId;
    private List<SaleItemRequest> items;
}