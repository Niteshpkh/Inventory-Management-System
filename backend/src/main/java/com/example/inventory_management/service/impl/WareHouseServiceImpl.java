package com.example.inventory_management.service.impl;

import com.example.inventory_management.dto.WareHouseRequest;
import com.example.inventory_management.dto.WareHouseResponse;
import com.example.inventory_management.entity.WareHouse;
import com.example.inventory_management.exception.ResourceNotFoundException;
import com.example.inventory_management.repository.WareHouseRepository;
import com.example.inventory_management.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {

    private static final String NOT_FOUND_MSG = "WareHouse not found with id: ";
    private final WareHouseRepository wareHouseRepository;

    @Override
    @Transactional
    public WareHouseResponse createWareHouse(WareHouseRequest request) {
        String cleanName = request.getName().trim();
        String code = request.getCode().trim();
        if (wareHouseRepository.existsByName(cleanName)) {
            throw new IllegalArgumentException("WareHouse with name '" + cleanName + "' already exists");
        }

        WareHouse wareHouse = WareHouse.builder()
                .name(cleanName)
                .code(code)
                .location(request.getLocation() != null ? request.getLocation().trim() : null)
                .active(true)
                .build();

        WareHouse saved = wareHouseRepository.save(wareHouse);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WareHouseResponse getWareHouseById(Long id) {
        WareHouse wareHouse = wareHouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));
        return mapToResponse(wareHouse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WareHouseResponse> getAllActiveWareHouses() {
        return wareHouseRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WareHouseResponse updateWareHouse(Long id, WareHouseRequest request) {
        WareHouse wareHouse = wareHouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));

        String cleanName = request.getName().trim();
        String code = request.getCode().trim();
        if (!wareHouse.getName().equalsIgnoreCase(cleanName) && wareHouseRepository.existsByName(cleanName)) {
            throw new IllegalArgumentException("WareHouse with name '" + cleanName + "' already exists");
        }

        wareHouse.setName(cleanName);
        wareHouse.setCode(code);
        wareHouse.setLocation(request.getLocation() != null ? request.getLocation().trim() : null);

        WareHouse updated = wareHouseRepository.save(wareHouse);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteWareHouse(Long id) {
        WareHouse wareHouse = wareHouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));
        wareHouse.setActive(false);
        wareHouseRepository.save(wareHouse);
    }

    private WareHouseResponse mapToResponse(WareHouse wareHouse) {
        return WareHouseResponse.builder()
                .id(wareHouse.getId())
                .name(wareHouse.getName())
                .location(wareHouse.getLocation())
                .active(wareHouse.isActive())
                .code(wareHouse.getCode())
                .build();
    }
}