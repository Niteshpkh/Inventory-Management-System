package com.example.inventory_management.service;

import com.example.inventory_management.dto.PurchaseRequest;
import com.example.inventory_management.dto.PurchaseResponse;

import java.util.List;

public interface PurchaseService {
    PurchaseResponse createPurchase(PurchaseRequest request);
    PurchaseResponse getPurchaseById(Long id);
    List<PurchaseResponse> getAllPurchases();
    PurchaseResponse receivePurchase(Long purchaseId);
}