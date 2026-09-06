package com.example.inventory_management.service;

import com.example.inventory_management.dto.SupplierRequest;
import com.example.inventory_management.dto.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse createSupplier(SupplierRequest request);
    SupplierResponse getSupplierById(Long id);
    List<SupplierResponse> getAllActiveSuppliers();
    SupplierResponse updateSupplier(Long id, SupplierRequest request);
    void deleteSupplier(Long id);
}