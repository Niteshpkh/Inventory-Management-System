package com.example.inventory_management.repository;

import com.example.inventory_management.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
    List<WareHouse> findByActiveTrue();
    boolean existsByName(String name);
    Optional<WareHouse> findByName(String name);
}