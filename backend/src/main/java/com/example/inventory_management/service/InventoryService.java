package com.example.inventory_management.service;

import com.example.inventory_management.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getAllInventory();
    InventoryResponse getInventoryById(Long id);
    InventoryResponse getInventoryByProductAndWarehouse(Long productId, Long warehouseId);
    List<InventoryResponse> getLowStockInventory();
}