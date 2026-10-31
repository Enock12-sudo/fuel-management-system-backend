package com.example.fuelmanagementsystem.repository;

import com.example.fuelmanagementsystem.entity.FuelIssue;
import com.example.fuelmanagementsystem.entity.FuelReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FuelReceiptRepository extends JpaRepository<FuelReceipt, UUID> {

    @Query("SELECT r FROM FuelReceipt r WHERE r.fuelIssue.driver.licenseNumber = :licenseNumber " +
            "ORDER BY r.receiptDate DESC")
    List<FuelReceipt> findDriverReceiptHistory(@Param("licenseNumber") String licenseNumber);

    boolean existsByFuelIssue(FuelIssue fuelIssue);

}
