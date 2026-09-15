package com.example.inventory_management.service;

import com.example.inventory_management.dto.SaleRequest;
import com.example.inventory_management.dto.SaleResponse;

import java.util.List;

public interface SaleService {
    SaleResponse createSale(SaleRequest request);
    SaleResponse getSaleById(Long id);
    List<SaleResponse> getAllSales();
}