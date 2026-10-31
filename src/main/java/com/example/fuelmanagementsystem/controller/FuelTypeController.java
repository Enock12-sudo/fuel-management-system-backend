package com.example.fuelmanagementsystem.controller;

import com.example.fuelmanagementsystem.entity.FuelType;
import com.example.fuelmanagementsystem.service.FuelTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/fuel-types")
public class FuelTypeController {

    private final FuelTypeService fuelTypeService;

    public FuelTypeController(FuelTypeService fuelTypeService) {
        this.fuelTypeService = fuelTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveFuelType(@RequestBody FuelType fuelType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fuelTypeService.saveFuelType(fuelType));
    }

    @GetMapping("/all")
    public List<FuelType> getAllFuelTypes() {
        return fuelTypeService.getAllFuelTypes();
    }

    @GetMapping("/unused")
    public List<FuelType> getUnusedFuelTypes() {
        return fuelTypeService.getUnusedFuelTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFuelTypeById(@PathVariable UUID id) {
        Optional<FuelType> fuelType = fuelTypeService.getFuelTypeById(id);
        if (fuelType.isPresent()) {
            return ResponseEntity.ok(fuelType.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The fuel type with that id does not exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFuelType(@PathVariable UUID id, @RequestBody FuelType fuelType) {
        String message = fuelTypeService.updateFuelType(id, fuelType);
        if (message.equals("The fuel type with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFuelType(@PathVariable UUID id) {
        String message = fuelTypeService.deleteFuelType(id);
        if (message.equals("The fuel type with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
