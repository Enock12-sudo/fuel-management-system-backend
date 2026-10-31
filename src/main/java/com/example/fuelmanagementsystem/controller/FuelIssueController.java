package com.example.fuelmanagementsystem.controller;

import com.example.fuelmanagementsystem.entity.FuelIssue;
import com.example.fuelmanagementsystem.entity.FuelIssueStatus;
import com.example.fuelmanagementsystem.service.FuelIssueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/fuel-issues")
public class FuelIssueController {

    private final FuelIssueService fuelIssueService;

    public FuelIssueController(FuelIssueService fuelIssueService) {
        this.fuelIssueService = fuelIssueService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveFuelIssue(@RequestBody FuelIssue fuelIssue) {
        String message = fuelIssueService.saveFuelIssue(fuelIssue);
        if (message.equals("Fuel issue date cannot be in the past")) {
            return ResponseEntity.badRequest().body(message);
        } else if (message.equals("Attendant already has a fuel issue on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }
    }

    @GetMapping("/all")
    public List<FuelIssue> getAllFuelIssues() {
        return fuelIssueService.getAllFuelIssues();
    }

    @GetMapping("/by-status")
    public List<FuelIssue> getFuelIssuesByStatus(@RequestParam FuelIssueStatus status) {
        return fuelIssueService.getFuelIssuesByStatus(status);
    }

    @GetMapping("/between")
    public List<FuelIssue> getFuelIssuesBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return fuelIssueService.getFuelIssuesBetween(startDate, endDate);
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> getFuelIssueStatsByStatus() {
        return fuelIssueService.getFuelIssueStatsByStatus();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFuelIssueById(@PathVariable UUID id) {
        Optional<FuelIssue> fuelIssue = fuelIssueService.getFuelIssueById(id);
        if (fuelIssue.isPresent()) {
            return ResponseEntity.ok(fuelIssue.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The fuel issue with that id does not exist");
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeFuelIssueStatus(
            @PathVariable UUID id,
            @RequestParam FuelIssueStatus status
    ) {
        String message = fuelIssueService.changeFuelIssueStatus(id, status);
        if (message.equals("The fuel issue with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("Fuel issue status updated successfully")) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelIssuesForDay(
            @RequestParam UUID pumpAttendantId,
            @RequestParam String date
    ) {
        LocalDate issueDate = LocalDate.parse(date);
        String message = fuelIssueService.cancelIssuesForDay(pumpAttendantId, issueDate);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFuelIssue(
            @PathVariable UUID id,
            @RequestBody FuelIssue fuelIssue
    ) {
        String message = fuelIssueService.updateFuelIssue(id, fuelIssue);
        if (message.equals("The fuel issue with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFuelIssue(@PathVariable UUID id) {
        String message = fuelIssueService.deleteFuelIssue(id);
        if (message.equals("The fuel issue with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("That fuel issue has fuel receipts, delete them first")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
