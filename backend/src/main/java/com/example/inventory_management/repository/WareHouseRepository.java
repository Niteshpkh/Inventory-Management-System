package com.example.inventory_management.repository;

import com.example.inventory_management.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
}