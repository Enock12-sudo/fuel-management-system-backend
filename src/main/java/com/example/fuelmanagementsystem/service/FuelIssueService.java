package com.example.fuelmanagementsystem.service;

import com.example.fuelmanagementsystem.entity.FuelIssue;
import com.example.fuelmanagementsystem.entity.FuelIssueStatus;
import com.example.fuelmanagementsystem.repository.FuelIssueRepository;
import com.example.fuelmanagementsystem.repository.FuelReceiptRepository;
import com.example.fuelmanagementsystem.repository.PumpAttendantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FuelIssueService {

    private final FuelIssueRepository fuelIssueRepository;
    private final FuelReceiptRepository fuelReceiptRepository;

    public FuelIssueService(
            FuelIssueRepository fuelIssueRepository,
            FuelReceiptRepository fuelReceiptRepository
    ) {
        this.fuelIssueRepository = fuelIssueRepository;
        this.fuelReceiptRepository = fuelReceiptRepository;
    }

    public String saveFuelIssue(FuelIssue fuelIssue) {
        if (fuelIssue.getIssueDate() != null && fuelIssue.getIssueDate().isBefore(LocalDate.now())) {
            return "Fuel issue date cannot be in the past";
        }

        if (fuelIssue.getAttendant() != null && fuelIssue.getIssueDate() != null) {
            boolean attendantIsBusy = fuelIssueRepository.existsByAttendantAndIssueDateAndStatusNot(
                    fuelIssue.getAttendant(),
                    fuelIssue.getIssueDate(),
                    FuelIssueStatus.DECLINED
            );
            if (attendantIsBusy) {
                return "Attendant already has a fuel issue on that date";
            }
        }

        fuelIssue.setStatus(FuelIssueStatus.REQUESTED);
        fuelIssueRepository.save(fuelIssue);
        return "Fuel issue saved successfully";
    }

    public List<FuelIssue> getAllFuelIssues() {
        return fuelIssueRepository.findAll();
    }

    public Optional<FuelIssue> getFuelIssueById(UUID id) {
        return fuelIssueRepository.findById(id);
    }

    public List<FuelIssue> getFuelIssuesByStatus(FuelIssueStatus status) {
        return fuelIssueRepository.findByStatusOrderByIssueDateAsc(status);
    }

    public List<FuelIssue> getFuelIssuesBetween(LocalDate start, LocalDate end) {
        return fuelIssueRepository.findByIssueDateBetweenOrderByIssueDateAsc(start, end);
    }

    public List<Object[]> getFuelIssueStatsByStatus() {
        return fuelIssueRepository.countFuelIssuesByStatus();
    }

    public String changeFuelIssueStatus(UUID id, FuelIssueStatus newStatus) {
        Optional<FuelIssue> fuelIssue = fuelIssueRepository.findById(id);
        if (fuelIssue.isEmpty()) {
            return "The fuel issue with that id does not exist";
        }

        FuelIssueStatus currentStatus = fuelIssue.get().getStatus();
        boolean allowed = false;

        if (currentStatus == FuelIssueStatus.REQUESTED) {
            if (newStatus == FuelIssueStatus.APPROVED || newStatus == FuelIssueStatus.DECLINED) {
                allowed = true;
            }
        } else if (currentStatus == FuelIssueStatus.APPROVED) {
            if (newStatus == FuelIssueStatus.DISPENSED || newStatus == FuelIssueStatus.DECLINED) {
                allowed = true;
            }
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        fuelIssue.get().setStatus(newStatus);
        fuelIssueRepository.save(fuelIssue.get());
        return "Fuel issue status updated successfully";
    }

    public String updateFuelIssue(UUID id, FuelIssue updatedFuelIssue) {
        Optional<FuelIssue> savedFuelIssue = fuelIssueRepository.findById(id);
        if (savedFuelIssue.isEmpty()) {
            return "The fuel issue with that id does not exist";
        }

        savedFuelIssue.get().setIssueDate(updatedFuelIssue.getIssueDate());
        savedFuelIssue.get().setPlateNumber(updatedFuelIssue.getPlateNumber());
        savedFuelIssue.get().setLitres(updatedFuelIssue.getLitres());
        savedFuelIssue.get().setDriver(updatedFuelIssue.getDriver());
        savedFuelIssue.get().setAttendant(updatedFuelIssue.getAttendant());
        fuelIssueRepository.save(savedFuelIssue.get());
        return "Fuel issue updated successfully";
    }

    @Transactional
    public String deleteFuelIssue(UUID id) {
        Optional<FuelIssue> fuelIssue = fuelIssueRepository.findById(id);
        if (fuelIssue.isEmpty()) {
            return "The fuel issue with that id does not exist";
        }

        if (fuelReceiptRepository.existsByFuelIssue(fuelIssue.get())) {
            return "That fuel issue has fuel receipts, delete them first";
        }

        fuelIssueRepository.delete(fuelIssue.get());
        return "Fuel issue deleted successfully";
    }

    @Transactional
    public String cancelIssuesForDay(UUID pumpAttendantId, LocalDate date) {
        int cancelledCount = fuelIssueRepository.declineIssuesForAttendantAndDate(pumpAttendantId, date);
        return cancelledCount + " fuel issues cancelled";
    }

}
