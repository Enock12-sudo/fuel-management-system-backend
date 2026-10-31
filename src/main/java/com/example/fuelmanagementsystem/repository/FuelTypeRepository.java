package com.example.fuelmanagementsystem.repository;

import com.example.fuelmanagementsystem.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FuelTypeRepository extends JpaRepository<FuelType, UUID> {

    @Query("SELECT f FROM FuelType f WHERE f.attendants IS EMPTY")
    List<FuelType> findUnusedFuelTypes();
}
