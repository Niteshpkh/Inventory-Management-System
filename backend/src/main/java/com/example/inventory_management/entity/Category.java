package com.example.inventory_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "categories")
public class Category {

    @Id
    private Long id;

    private String name;

    private String description;

    private boolean isActive;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

}