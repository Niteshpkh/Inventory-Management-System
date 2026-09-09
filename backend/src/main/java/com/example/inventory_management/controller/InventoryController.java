package com.example.inventory_management.controller;

import com.example.inventory_management.dto.InventoryResponse;
import com.example.inventory_management.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<InventoryResponse> getByProductAndWarehouse(
            @RequestParam Long productId,
            @RequestParam Long warehouseId
    ) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponse>> getLowStockInventory() {
        return ResponseEntity.ok(inventoryService.getLowStockInventory());
    }
}