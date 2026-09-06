package com.example.inventory_management.service.impl;

import com.example.inventory_management.dto.SupplierRequest;
import com.example.inventory_management.dto.SupplierResponse;
import com.example.inventory_management.entity.Supplier;
import com.example.inventory_management.exception.ResourceNotFoundException;
import com.example.inventory_management.repository.SupplierRepository;
import com.example.inventory_management.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private static final String SUPPLIER_NOT_FOUND = "Supplier not found with id: ";
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        if (request.getEmail() != null && !request.getEmail().isBlank() && supplierRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Supplier with email '" + request.getEmail() + "' already exists");
        }

        Supplier supplier = Supplier.builder()
                .name(request.getName().trim())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .isActive(true)
                .build();

        Supplier saved = supplierRepository.save(supplier);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_NOT_FOUND + id));
        return mapToResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllActiveSuppliers() {
        return supplierRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_NOT_FOUND + id));

        supplier.setName(request.getName().trim());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());

        Supplier updated = supplierRepository.save(supplier);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUPPLIER_NOT_FOUND + id));
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    private SupplierResponse mapToResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .isActive(supplier.isActive())
                .build();
    }
}