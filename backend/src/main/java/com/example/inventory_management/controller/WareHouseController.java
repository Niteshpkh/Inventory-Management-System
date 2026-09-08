package com.example.inventory_management.controller;

import com.example.inventory_management.dto.WareHouseRequest;
import com.example.inventory_management.dto.WareHouseResponse;
import com.example.inventory_management.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WareHouseController {

    private final WareHouseService wareHouseService;

    @PostMapping
    public ResponseEntity<WareHouseResponse> createWareHouse(@RequestBody WareHouseRequest request) {
        WareHouseResponse response = wareHouseService.createWareHouse(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WareHouseResponse> getWareHouseById(@PathVariable Long id) {
        return ResponseEntity.ok(wareHouseService.getWareHouseById(id));
    }

    @GetMapping
    public ResponseEntity<List<WareHouseResponse>> getAllActiveWareHouses() {
        return ResponseEntity.ok(wareHouseService.getAllActiveWareHouses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WareHouseResponse> updateWareHouse(
            @PathVariable Long id,
            @RequestBody WareHouseRequest request
    ) {
        return ResponseEntity.ok(wareHouseService.updateWareHouse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWareHouse(@PathVariable Long id) {
        wareHouseService.deleteWareHouse(id);
        return ResponseEntity.noContent().build();
    }
}