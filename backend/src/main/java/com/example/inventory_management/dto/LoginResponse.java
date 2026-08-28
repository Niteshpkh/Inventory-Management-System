package com.example.inventory_management.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String message;
    private String username;

    public LoginResponse(String loginSuccessful, String token) {
    }
}

