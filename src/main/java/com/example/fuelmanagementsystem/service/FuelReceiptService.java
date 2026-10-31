package com.example.fuelmanagementsystem.service;

import com.example.fuelmanagementsystem.entity.FuelIssue;
import com.example.fuelmanagementsystem.entity.FuelIssueStatus;
import com.example.fuelmanagementsystem.entity.FuelReceipt;
import com.example.fuelmanagementsystem.repository.FuelIssueRepository;
import com.example.fuelmanagementsystem.repository.FuelReceiptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FuelReceiptService {

    private final FuelReceiptRepository fuelReceiptRepository;
    private final FuelIssueRepository fuelIssueRepository;

    public FuelReceiptService(FuelReceiptRepository fuelReceiptRepository, FuelIssueRepository fuelIssueRepository) {
        this.fuelReceiptRepository = fuelReceiptRepository;
        this.fuelIssueRepository = fuelIssueRepository;
    }

    public String saveFuelReceipt(UUID fuelIssueId, FuelReceipt fuelReceipt) {
        Optional<FuelIssue> fuelIssue = fuelIssueRepository.findById(fuelIssueId);
        if (fuelIssue.isEmpty()) {
            return "The fuel issue with that id does not exist";
        }

        if (fuelIssue.get().getStatus() != FuelIssueStatus.DISPENSED) {
            return "Fuel receipts are only for DISPENSED fuel issues";
        }

        fuelReceipt.setReceiptDate(LocalDate.now());
        fuelReceipt.setFuelIssue(fuelIssue.get());
        fuelReceiptRepository.save(fuelReceipt);
        return "Fuel receipt saved successfully";
    }

    public List<FuelReceipt> getFuelReceiptHistory(String licenseNumber) {
        return fuelReceiptRepository.findDriverReceiptHistory(licenseNumber);
    }

    public Optional<FuelReceipt> getFuelReceiptById(UUID id) {
        return fuelReceiptRepository.findById(id);
    }

    public List<FuelReceipt> getAllFuelReceipts() {
        return fuelReceiptRepository.findAll();
    }

    public String deleteFuelReceipt(UUID id) {
        Optional<FuelReceipt> fuelReceipt = fuelReceiptRepository.findById(id);
        if (fuelReceipt.isEmpty()) {
            return "The fuel receipt with that id does not exist";
        }

        fuelReceiptRepository.delete(fuelReceipt.get());
        return "Fuel receipt deleted successfully";
    }

}
