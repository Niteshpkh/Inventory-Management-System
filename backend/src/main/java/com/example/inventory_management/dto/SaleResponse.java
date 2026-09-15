package com.example.inventory_management.dto;

import com.example.inventory_management.enums.SaleStatus; // verify this package matches your project
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponse {
    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private String warehouseName;
    private SaleStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime saleDate;
    private List<SaleItemResponse> items;
}