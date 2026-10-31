package com.example.fuelmanagementsystem.repository;

import com.example.fuelmanagementsystem.entity.Pump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PumpRepository extends JpaRepository<Pump, UUID> {

    Optional<Pump> findByPumpNumber(int pumpNumber);

    List<Pump> findByOrderByPumpNumberAsc();

    @Query("SELECT p FROM Pump p WHERE NOT EXISTS (SELECT a FROM PumpAttendant a WHERE a.pump = p) ORDER BY p.pumpNumber")
    List<Pump> findFreePumps();
}
