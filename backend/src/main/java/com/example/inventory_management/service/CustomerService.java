package com.example.inventory_management.service;

import com.example.inventory_management.dto.CustomerRequest;
import com.example.inventory_management.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getCustomerById(Long id);
    List<CustomerResponse> getAllActiveCustomers();
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    void deleteCustomer(Long id);
}