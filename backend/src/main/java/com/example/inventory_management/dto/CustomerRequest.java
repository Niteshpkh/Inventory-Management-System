package com.example.inventory_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
}