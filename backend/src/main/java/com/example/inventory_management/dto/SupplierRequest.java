package com.example.inventory_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
}