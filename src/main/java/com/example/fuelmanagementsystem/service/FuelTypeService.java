package com.example.fuelmanagementsystem.service;

import com.example.fuelmanagementsystem.entity.FuelType;
import com.example.fuelmanagementsystem.repository.FuelTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;

    public FuelTypeService(FuelTypeRepository fuelTypeRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
    }

    public String saveFuelType(FuelType fuelType) {
        fuelTypeRepository.save(fuelType);
        return "Fuel type saved successfully";
    }

    public List<FuelType> getAllFuelTypes() {
        return fuelTypeRepository.findAll();
    }

    public Optional<FuelType> getFuelTypeById(UUID id) {
        return fuelTypeRepository.findById(id);
    }

    public List<FuelType> getUnusedFuelTypes() {
        return fuelTypeRepository.findUnusedFuelTypes();
    }

    public String updateFuelType(UUID id, FuelType updatedFuelType) {
        Optional<FuelType> savedFuelType = fuelTypeRepository.findById(id);
        if (savedFuelType.isEmpty()) {
            return "The fuel type with that id does not exist";
        }

        savedFuelType.get().setName(updatedFuelType.getName());
        fuelTypeRepository.save(savedFuelType.get());
        return "Fuel type updated successfully";
    }

    public String deleteFuelType(UUID id) {
        Optional<FuelType> fuelType = fuelTypeRepository.findById(id);
        if (fuelType.isEmpty()) {
            return "The fuel type with that id does not exist";
        }

        fuelTypeRepository.delete(fuelType.get());
        return "Fuel type deleted successfully";
    }
}
