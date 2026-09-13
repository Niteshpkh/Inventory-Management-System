package com.example.inventory_management.service;

import com.example.inventory_management.dto.StockAdjustmentRequest;
import com.example.inventory_management.dto.StockTransactionResponse;
import com.example.inventory_management.entity.Product;
import com.example.inventory_management.entity.WareHouse;
import com.example.inventory_management.enums.TransactionType;

import java.util.List;

public interface StockTransactionService {
    StockTransactionResponse adjustStock(StockAdjustmentRequest request);
    void logTransaction(Product product, WareHouse warehouse, com.example.inventory_management.enums.TransactionType type, Integer quantity, Integer balanceAfter, String referenceNumber, String note);
    List<StockTransactionResponse> getTransactionsByProductAndWarehouse(Long productId, Long warehouseId);
    List<StockTransactionResponse> getAllTransactions();
}