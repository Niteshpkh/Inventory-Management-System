package com.example.inventory_management.controller;

import com.example.inventory_management.dto.LoginRequest;
import com.example.inventory_management.dto.LoginResponse;
import com.example.inventory_management.Security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public PublicController(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

        String token = jwtService.generateToken(
                authentication.getName()
        );

        LoginResponse response =
                new LoginResponse("Login successful", token);

        return ResponseEntity.ok(response);
    }



}
