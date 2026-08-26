package com.example.inventory_management.dto;

import com.example.inventory_management.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
}
