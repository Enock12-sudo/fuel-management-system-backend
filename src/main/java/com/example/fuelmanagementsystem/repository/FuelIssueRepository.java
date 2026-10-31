package com.example.fuelmanagementsystem.repository;

import com.example.fuelmanagementsystem.entity.FuelIssue;
import com.example.fuelmanagementsystem.entity.FuelIssueStatus;
import com.example.fuelmanagementsystem.entity.PumpAttendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FuelIssueRepository extends JpaRepository<FuelIssue, UUID> {

    List<FuelIssue> findByStatusOrderByIssueDateAsc(FuelIssueStatus status);

    List<FuelIssue> findByIssueDateBetweenOrderByIssueDateAsc(LocalDate start, LocalDate end);

    boolean existsByAttendantAndIssueDateAndStatusNot(
            PumpAttendant attendant,
            LocalDate issueDate,
            FuelIssueStatus status
    );

    List<FuelIssue> findByAttendantAndIssueDate(PumpAttendant attendant, LocalDate issueDate);

    long countByStatus(FuelIssueStatus status);

    long countByIssueDate(LocalDate issueDate);

    @Query("SELECT i.status, COUNT(i) FROM FuelIssue i GROUP BY i.status")
    List<Object[]> countFuelIssuesByStatus();

    @Modifying
    @Transactional
    @Query("UPDATE FuelIssue i SET i.status = com.example.fuelmanagementsystem.entity.FuelIssueStatus.DECLINED " +
            "WHERE i.attendant.id = :attendantId AND i.issueDate = :issueDate " +
            "AND i.status <> com.example.fuelmanagementsystem.entity.FuelIssueStatus.DISPENSED")
    int declineIssuesForAttendantAndDate(
            @Param("attendantId") UUID attendantId,
            @Param("issueDate") LocalDate issueDate
    );
}
