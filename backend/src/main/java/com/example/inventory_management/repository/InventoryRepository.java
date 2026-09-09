package com.example.inventory_management.repository;

import com.example.inventory_management.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    List<Inventory> findByProductId(Long productId);

    List<Inventory> findByWarehouseId(Long warehouseId);

    // Custom JPQL query for low-stock alert monitoring
    @Query("SELECT i FROM Inventory i JOIN i.product p WHERE i.quantity <= p.reorderLevel")
    List<Inventory> findLowStockInventories();
}