package com.example.inventory_management.service.impl;

import com.example.inventory_management.dto.CustomerRequest;
import com.example.inventory_management.dto.CustomerResponse;
import com.example.inventory_management.entity.Customer;
import com.example.inventory_management.exception.ResourceNotFoundException;
import com.example.inventory_management.repository.CustomerRepository;
import com.example.inventory_management.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMER_NOT_FOUND = "Customer not found with id: ";
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        // Safe check: Only validate uniqueness if email is provided
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String cleanEmail = request.getEmail().trim().toLowerCase();
            if (customerRepository.existsByEmail(cleanEmail)) {
                throw new IllegalArgumentException("Customer with email '" + cleanEmail + "' already exists");
            }
        }

        Customer customer = Customer.builder()
                .name(request.getName().trim())
                .phone(request.getPhone())
                .email(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null)
                .address(request.getAddress())
                .active(true)
                .build();

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND + id));
        return mapToResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllActiveCustomers() {
        return customerRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND + id));

        customer.setName(request.getName().trim());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null);
        customer.setAddress(request.getAddress());

        Customer updated = customerRepository.save(customer);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND + id));
        customer.setActive(false);
        customerRepository.save(customer);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .active(customer.isActive())
                .build();
    }
}