package com.example.inventory_management.repository;

import com.example.inventory_management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByActiveTrue();
    boolean existsByEmail(String email);
    Optional<Customer> findByPhone(String phone);
}