package com.example.inventory_management.service.impl;

import com.example.inventory_management.dto.SaleItemRequest;
import com.example.inventory_management.dto.SaleItemResponse;
import com.example.inventory_management.dto.SaleRequest;
import com.example.inventory_management.dto.SaleResponse;
import com.example.inventory_management.entity.*;
import com.example.inventory_management.enums.SaleStatus;
import com.example.inventory_management.enums.TransactionType;
import com.example.inventory_management.exception.ResourceNotFoundException;
import com.example.inventory_management.repository.*;
import com.example.inventory_management.service.SaleService;
import com.example.inventory_management.service.StockTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final WareHouseRepository wareHouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final StockTransactionService stockTransactionService;

    @Override
    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        // Resolve invoice number: use client provided value or generate a fallback
        String invoiceNumber = request.getInvoiceNumber();
        if (invoiceNumber == null || invoiceNumber.trim().isEmpty()) {
            invoiceNumber = "INV-" + System.currentTimeMillis();
        } else if (saleRepository.existsByInvoiceNumber(invoiceNumber)) {
            throw new IllegalArgumentException("Sale invoice '" + invoiceNumber + "' already exists");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        WareHouse warehouse = wareHouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + request.getWarehouseId()));

        Sale sale = Sale.builder()
                .invoiceNumber(invoiceNumber)
                .customer(customer)
                .warehouse(warehouse)
                .status(SaleStatus.COMPLETED)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal runningTotal = BigDecimal.ZERO;

        for (SaleItemRequest itemDto : request.getItems()) {
            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

            // 1. Check stock existence in designated warehouse
            Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                    .orElseThrow(() -> new IllegalStateException("No inventory record found for product '" + product.getName() + "' at " + warehouse.getName()));

            // 2. Prevent negative stock
            if (inventory.getQuantity() < itemDto.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product '" + product.getName() +
                        "'. Available: " + inventory.getQuantity() + ", Requested: " + itemDto.getQuantity());
            }

            // 3. Decrement Inventory
            inventory.setQuantity(inventory.getQuantity() - itemDto.getQuantity());
            Inventory updatedInventory = inventoryRepository.save(inventory);

            // 4. Log to StockTransaction audit ledger
            stockTransactionService.logTransaction(
                    product,
                    warehouse,
                    TransactionType.SALE_OUT,
                    itemDto.getQuantity(),
                    updatedInventory.getQuantity(),
                    invoiceNumber,
                    "Customer Sale Completed"
            );

            // 5. Calculate financials with price snapshot
            BigDecimal subTotal = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            runningTotal = runningTotal.add(subTotal);

            SaleItem item = SaleItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice())
                    .subTotal(subTotal)
                    .build();

            sale.addItem(item);
        }

        sale.setTotalAmount(runningTotal);
        Sale savedSale = saleRepository.save(sale);

        return mapToResponse(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale invoice not found with id: " + id));
        return mapToResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SaleResponse mapToResponse(Sale sale) {
        List<SaleItemResponse> itemResponses = sale.getItems().stream()
                .map(item -> SaleItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .productSku(item.getProduct().getSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subTotal(item.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        return SaleResponse.builder()
                .id(sale.getId())
                .invoiceNumber(sale.getInvoiceNumber())
                .customerId(sale.getCustomer().getId())
                .customerName(sale.getCustomer().getName())
                .warehouseId(sale.getWarehouse().getId())
                .warehouseName(sale.getWarehouse().getName())
                .status(sale.getStatus())
                .totalAmount(sale.getTotalAmount())
                .saleDate(sale.getSaleDate())
                .items(itemResponses)
                .build();
    }
}