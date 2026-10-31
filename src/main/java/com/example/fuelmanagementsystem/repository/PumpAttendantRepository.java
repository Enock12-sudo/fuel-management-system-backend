package com.example.fuelmanagementsystem.repository;

import com.example.fuelmanagementsystem.entity.Pump;
import com.example.fuelmanagementsystem.entity.PumpAttendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PumpAttendantRepository extends JpaRepository<PumpAttendant, UUID> {

    @Query("SELECT DISTINCT a FROM PumpAttendant a JOIN a.fuelTypes f WHERE LOWER(f.name) = LOWER(:name)")
    List<PumpAttendant> findByFuelTypeName(@Param("name") String name);

    boolean existsByPump(Pump pump);
}
