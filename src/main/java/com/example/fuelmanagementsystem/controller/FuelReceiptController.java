package com.example.fuelmanagementsystem.controller;

import com.example.fuelmanagementsystem.entity.FuelReceipt;
import com.example.fuelmanagementsystem.service.FuelReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/fuel-receipts")
public class FuelReceiptController {

    private final FuelReceiptService fuelReceiptService;

    public FuelReceiptController(FuelReceiptService fuelReceiptService) {
        this.fuelReceiptService = fuelReceiptService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveFuelReceipt(
            @RequestParam UUID fuelIssueId,
            @RequestBody FuelReceipt fuelReceipt
    ) {
        String message = fuelReceiptService.saveFuelReceipt(fuelIssueId, fuelReceipt);
        if (message.equals("The fuel issue with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("Fuel receipts are only for DISPENSED fuel issues")) {
            return ResponseEntity.badRequest().body(message);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
    }

    @GetMapping("/driver/{licenseNumber}")
    public List<FuelReceipt> getFuelReceiptHistory(@PathVariable String licenseNumber) {
        return fuelReceiptService.getFuelReceiptHistory(licenseNumber);
    }

    @GetMapping("/all")
    public List<FuelReceipt> getAllFuelReceipts() {
        return fuelReceiptService.getAllFuelReceipts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFuelReceiptById(@PathVariable UUID id) {
        Optional<FuelReceipt> fuelReceipt = fuelReceiptService.getFuelReceiptById(id);
        if (fuelReceipt.isPresent()) {
            return ResponseEntity.ok(fuelReceipt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The fuel receipt with that id does not exist");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFuelReceipt(@PathVariable UUID id) {
        String message = fuelReceiptService.deleteFuelReceipt(id);
        if (message.equals("The fuel receipt with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
