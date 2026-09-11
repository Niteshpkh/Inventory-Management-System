package com.example.inventory_management.service.impl;

import com.example.inventory_management.dto.PurchaseItemRequest;
import com.example.inventory_management.dto.PurchaseItemResponse;
import com.example.inventory_management.dto.PurchaseRequest;
import com.example.inventory_management.dto.PurchaseResponse;
import com.example.inventory_management.entity.*;
import com.example.inventory_management.enums.PurchaseStatus;
import com.example.inventory_management.exception.ResourceNotFoundException;
import com.example.inventory_management.repository.*;
import com.example.inventory_management.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final WareHouseRepository wareHouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        if (purchaseRepository.existsByReferenceNumber(request.getReferenceNumber())) {
            throw new IllegalArgumentException("Purchase order with reference '" + request.getReferenceNumber() + "' already exists");
        }

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        WareHouse wareHouse = wareHouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + request.getWarehouseId()));

        Purchase purchase = Purchase.builder()
                .referenceNumber(request.getReferenceNumber())
                .supplier(supplier)
                .warehouse(wareHouse)
                .status(PurchaseStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal runningTotal = BigDecimal.ZERO;

        for (PurchaseItemRequest itemDto : request.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

            BigDecimal subTotal = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            runningTotal = runningTotal.add(subTotal);

            PurchaseItem item = PurchaseItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice())
                    .subTotal(subTotal)
                    .build();

            purchase.addItem(item);
        }

        purchase.setTotalAmount(runningTotal);

        Purchase saved = purchaseRepository.save(purchase);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
        return mapToResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseResponse receivePurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + purchaseId));

        if (purchase.getStatus() == PurchaseStatus.RECEIVED) {
            throw new IllegalStateException("Purchase order is already marked as RECEIVED");
        }

        if (purchase.getStatus() == PurchaseStatus.CANCELLED) {
            throw new IllegalStateException("Cannot receive a CANCELLED purchase order");
        }

        for (PurchaseItem item : purchase.getItems()) {
            Long productId = item.getProduct().getId();
            Long warehouseId = purchase.getWarehouse().getId();

            Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                    .orElseGet(() -> Inventory.builder()
                            .product(item.getProduct())
                            .warehouse(purchase.getWarehouse())
                            .quantity(0)
                            .build());

            inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
            inventoryRepository.save(inventory);
        }

        purchase.setStatus(PurchaseStatus.RECEIVED);
        Purchase updated = purchaseRepository.save(purchase);

        return mapToResponse(updated);
    }

    private PurchaseResponse mapToResponse(Purchase purchase) {
        List<PurchaseItemResponse> itemResponses = purchase.getItems().stream()
                .map(item -> PurchaseItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .productSku(item.getProduct().getSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subTotal(item.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        return PurchaseResponse.builder()
                .id(purchase.getId())
                .referenceNumber(purchase.getReferenceNumber())
                .supplierId(purchase.getSupplier().getId())
                .supplierName(purchase.getSupplier().getName())
                .warehouseId(purchase.getWarehouse().getId())
                .warehouseName(purchase.getWarehouse().getName())
                .status(purchase.getStatus())
                .totalAmount(purchase.getTotalAmount())
                .purchaseDate(purchase.getPurchaseDate())
                .items(itemResponses)
                .build();
    }
}