package com.example.inventory_management.service;

import com.example.inventory_management.dto.WareHouseRequest;
import com.example.inventory_management.dto.WareHouseResponse;

import java.util.List;

public interface WareHouseService {
    WareHouseResponse createWareHouse(WareHouseRequest request);
    WareHouseResponse getWareHouseById(Long id);
    List<WareHouseResponse> getAllActiveWareHouses();
    WareHouseResponse updateWareHouse(Long id, WareHouseRequest request);
    void deleteWareHouse(Long id);
}