package com.example.fuelmanagementsystem.repository;

import com.example.fuelmanagementsystem.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    List<Driver> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT i.driver FROM FuelIssue i WHERE i.attendant.id = :attendantId")
    List<Driver> findDriversByPumpAttendantId(@Param("attendantId") UUID attendantId);
}
