package com.example.inventory_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WareHouseResponse {
    private Long id;
    private String name;
    private String location;
    private boolean active;
    private String code;
}