package com.example.inventory_management.service.impl;

import com.example.inventory_management.dto.StockAdjustmentRequest;
import com.example.inventory_management.dto.StockTransactionResponse;
import com.example.inventory_management.entity.*;
import com.example.inventory_management.enums.TransactionType;
import com.example.inventory_management.exception.ResourceNotFoundException;
import com.example.inventory_management.repository.InventoryRepository;
import com.example.inventory_management.repository.ProductRepository;
import com.example.inventory_management.repository.StockTransactionRepository;
import com.example.inventory_management.repository.WareHouseRepository;
import com.example.inventory_management.service.StockTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockTransactionServiceImpl implements StockTransactionService {

    private final StockTransactionRepository stockTransactionRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WareHouseRepository wareHouseRepository;

    @Override
    @Transactional
    public StockTransactionResponse adjustStock(StockAdjustmentRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Adjustment quantity must be greater than zero");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        WareHouse warehouse = wareHouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + request.getWarehouseId()));

        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                .orElseGet(() -> Inventory.builder()
                        .product(product)
                        .warehouse(warehouse)
                        .quantity(0)
                        .build());

        int newBalance;
        if (request.getTransactionType() == TransactionType.ADJUSTMENT_IN) {
            newBalance = inventory.getQuantity() + request.getQuantity();
        } else if (request.getTransactionType() == TransactionType.ADJUSTMENT_OUT || request.getTransactionType() == TransactionType.DAMAGE_OUT) {
            if (inventory.getQuantity() < request.getQuantity()) {
                throw new IllegalStateException("Insufficient inventory to adjust out. Available: " + inventory.getQuantity() + ", requested: " + request.getQuantity());
            }
            newBalance = inventory.getQuantity() - request.getQuantity();
        } else {
            throw new IllegalArgumentException("Invalid manual adjustment transaction type: " + request.getTransactionType());
        }

        inventory.setQuantity(newBalance);
        inventoryRepository.save(inventory);

        String refNumber = "ADJ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        StockTransaction transaction = StockTransaction.builder()
                .product(product)
                .warehouse(warehouse)
                .transactionType(request.getTransactionType())
                .quantity(request.getQuantity())
                .balanceAfter(newBalance)
                .referenceNumber(refNumber)
                .note(request.getNote())
                .build();

        StockTransaction saved = stockTransactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void logTransaction(Product product, WareHouse warehouse, TransactionType type, Integer quantity, Integer balanceAfter, String referenceNumber, String note) {
        StockTransaction transaction = StockTransaction.builder()
                .product(product)
                .warehouse(warehouse)
                .transactionType(type)
                .quantity(quantity)
                .balanceAfter(balanceAfter)
                .referenceNumber(referenceNumber)
                .note(note)
                .build();
        stockTransactionRepository.save(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransactionResponse> getTransactionsByProductAndWarehouse(Long productId, Long warehouseId) {
        return stockTransactionRepository.findByProductIdAndWarehouseIdOrderByCreatedAtDesc(productId, warehouseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransactionResponse> getAllTransactions() {
        return stockTransactionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private StockTransactionResponse mapToResponse(StockTransaction st) {
        return StockTransactionResponse.builder()
                .id(st.getId())
                .productId(st.getProduct().getId())
                .productName(st.getProduct().getName())
                .productSku(st.getProduct().getSku())
                .warehouseId(st.getWarehouse().getId())
                .warehouseName(st.getWarehouse().getName())
                .transactionType(st.getTransactionType())
                .quantity(st.getQuantity())
                .balanceAfter(st.getBalanceAfter())
                .referenceNumber(st.getReferenceNumber())
                .note(st.getNote())
                .createdAt(st.getCreatedAt())
                .build();
    }
}