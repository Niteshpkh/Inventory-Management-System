package com.example.inventory_management.repository;

import com.example.inventory_management.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByIsActiveTrue();
    boolean existsByEmail(String email);
}