package com.example.inventory_management.repository;

import com.example.inventory_management.entity.Purchase;

import com.example.inventory_management.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findByReferenceNumber(String referenceNumber);
    boolean existsByReferenceNumber(String referenceNumber);
    List<Purchase> findByStatus(com.example.inventory_management.enums.PurchaseStatus status);
    List<Purchase> findByWarehouseId(Long warehouseId);
}