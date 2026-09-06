package com.example.inventory_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private boolean isActive;
}