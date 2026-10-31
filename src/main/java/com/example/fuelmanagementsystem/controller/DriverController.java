package com.example.fuelmanagementsystem.controller;

import com.example.fuelmanagementsystem.entity.Driver;
import com.example.fuelmanagementsystem.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveDriver(@RequestBody Driver driver) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.saveDriver(driver));
    }

    @GetMapping("/all")
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/by-last-name")
    public List<Driver> getDriversByLastName(@RequestParam String lastName) {
        return driverService.getDriversByLastName(lastName);
    }

    @GetMapping("/of-pump-attendant/{pumpAttendantId}")
    public ResponseEntity<?> getDriversOfPumpAttendant(@PathVariable UUID pumpAttendantId) {
        if (driverService.getPumpAttendantById(pumpAttendantId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The pump attendant with that id does not exist");
        }
        return ResponseEntity.ok(driverService.getDriversOfPumpAttendant(pumpAttendantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDriverById(@PathVariable UUID id) {
        Optional<Driver> driver = driverService.getDriverById(id);
        if (driver.isPresent()) {
            return ResponseEntity.ok(driver.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The driver with that id does not exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDriver(@PathVariable UUID id, @RequestBody Driver driver) {
        String message = driverService.updateDriver(id, driver);
        if (message.equals("The driver with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable UUID id) {
        String message = driverService.deleteDriver(id);
        if (message.equals("The driver with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
