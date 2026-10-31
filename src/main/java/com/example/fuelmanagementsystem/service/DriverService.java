package com.example.fuelmanagementsystem.service;

import com.example.fuelmanagementsystem.entity.Driver;
import com.example.fuelmanagementsystem.entity.PumpAttendant;
import com.example.fuelmanagementsystem.repository.DriverRepository;
import com.example.fuelmanagementsystem.repository.PumpAttendantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final PumpAttendantRepository pumpAttendantRepository;

    public DriverService(DriverRepository driverRepository, PumpAttendantRepository pumpAttendantRepository) {
        this.driverRepository = driverRepository;
        this.pumpAttendantRepository = pumpAttendantRepository;
    }

    public String saveDriver(Driver driver) {
        driverRepository.save(driver);
        return "Driver saved successfully";
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Driver> getDriverById(UUID id) {
        return driverRepository.findById(id);
    }

    public List<Driver> getDriversByLastName(String lastName) {
        return driverRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Driver> getDriversOfPumpAttendant(UUID pumpAttendantId) {
        return driverRepository.findDriversByPumpAttendantId(pumpAttendantId);
    }

    public Optional<PumpAttendant> getPumpAttendantById(UUID id) {
        return pumpAttendantRepository.findById(id);
    }

    public String updateDriver(UUID id, Driver updatedDriver) {
        Optional<Driver> savedDriver = driverRepository.findById(id);
        if (savedDriver.isEmpty()) {
            return "The driver with that id does not exist";
        }

        savedDriver.get().setFirstName(updatedDriver.getFirstName());
        savedDriver.get().setLastName(updatedDriver.getLastName());
        savedDriver.get().setLicenseNumber(updatedDriver.getLicenseNumber());
        savedDriver.get().setPhone(updatedDriver.getPhone());
        driverRepository.save(savedDriver.get());
        return "Driver updated successfully";
    }

    public String deleteDriver(UUID id) {
        Optional<Driver> driver = driverRepository.findById(id);
        if (driver.isEmpty()) {
            return "The driver with that id does not exist";
        }

        driverRepository.delete(driver.get());
        return "Driver deleted successfully";
    }
}
