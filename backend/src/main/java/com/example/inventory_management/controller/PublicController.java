package com.example.inventory_management.controller;

import com.example.inventory_management.dto.LoginRequest;
import com.example.inventory_management.dto.LoginResponse;
import com.example.inventory_management.Security.JwtService;
import com.example.inventory_management.dto.RegisterRequest;
import com.example.inventory_management.entity.User;
import com.example.inventory_management.enums.Role;
import com.example.inventory_management.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public")
public class PublicController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public PublicController(
            AuthenticationManager authenticationManager,
            JwtService jwtService, PasswordEncoder passwordEncoder, UserRepository userRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Never store plain text!
                .role(request.getRole() != null ? request.getRole() : Role.ADMIN)
                .isActive(true)
                .build();

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
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
