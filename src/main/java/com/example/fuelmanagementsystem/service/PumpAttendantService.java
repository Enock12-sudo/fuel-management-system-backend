package com.example.fuelmanagementsystem.service;

import com.example.fuelmanagementsystem.entity.FuelType;
import com.example.fuelmanagementsystem.entity.Pump;
import com.example.fuelmanagementsystem.entity.PumpAttendant;
import com.example.fuelmanagementsystem.repository.FuelTypeRepository;
import com.example.fuelmanagementsystem.repository.PumpAttendantRepository;
import com.example.fuelmanagementsystem.repository.PumpRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PumpAttendantService {

    private final PumpAttendantRepository pumpAttendantRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final PumpRepository pumpRepository;

    public PumpAttendantService(
            PumpAttendantRepository pumpAttendantRepository,
            FuelTypeRepository fuelTypeRepository,
            PumpRepository pumpRepository
    ) {
        this.pumpAttendantRepository = pumpAttendantRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.pumpRepository = pumpRepository;
    }

    public String savePumpAttendant(PumpAttendant pumpAttendant) {
        pumpAttendantRepository.save(pumpAttendant);
        return "Pump attendant saved successfully";
    }

    public List<PumpAttendant> getAllPumpAttendants() {
        return pumpAttendantRepository.findAll();
    }

    public Optional<PumpAttendant> getPumpAttendantById(UUID id) {
        return pumpAttendantRepository.findById(id);
    }

    public List<PumpAttendant> getPumpAttendantsByFuelType(String name) {
        return pumpAttendantRepository.findByFuelTypeName(name);
    }

    public String addFuelType(UUID pumpAttendantId, UUID fuelTypeId) {
        Optional<PumpAttendant> attendant = pumpAttendantRepository.findById(pumpAttendantId);
        if (attendant.isEmpty()) {
            return "The pump attendant with that id does not exist";
        }

        Optional<FuelType> fuelType = fuelTypeRepository.findById(fuelTypeId);
        if (fuelType.isEmpty()) {
            return "That fuel type does not exist";
        }

        for (FuelType savedFuelType : attendant.get().getFuelTypes()) {
            if (savedFuelType.getId().equals(fuelType.get().getId())) {
                return "The pump attendant already has that fuel type";
            }
        }

        attendant.get().getFuelTypes().add(fuelType.get());
        pumpAttendantRepository.save(attendant.get());
        return "Fuel type added to the pump attendant";
    }

    public String changePump(UUID pumpAttendantId, int pumpNumber) {
        Optional<PumpAttendant> attendant = pumpAttendantRepository.findById(pumpAttendantId);
        if (attendant.isEmpty()) {
            return "The pump attendant with that id does not exist";
        }

        Optional<Pump> newPump = pumpRepository.findByPumpNumber(pumpNumber);
        if (newPump.isEmpty()) {
            return "The pump with that pumpNumber does not exist";
        }

        Pump currentPump = attendant.get().getPump();
        boolean pumpBelongsToAnotherAttendant = pumpAttendantRepository.existsByPump(newPump.get())
                && (currentPump == null || !currentPump.getId().equals(newPump.get().getId()));
        if (pumpBelongsToAnotherAttendant) {
            return "That pump is taken by another pump attendant";
        }

        attendant.get().setPump(newPump.get());
        pumpAttendantRepository.save(attendant.get());
        return "Pump changed successfully";
    }

    public String updatePumpAttendant(UUID id, PumpAttendant updatedAttendant) {
        Optional<PumpAttendant> savedAttendant = pumpAttendantRepository.findById(id);
        if (savedAttendant.isEmpty()) {
            return "The pump attendant with that id does not exist";
        }

        savedAttendant.get().setFirstName(updatedAttendant.getFirstName());
        savedAttendant.get().setLastName(updatedAttendant.getLastName());
        savedAttendant.get().setPhone(updatedAttendant.getPhone());
        savedAttendant.get().setPump(updatedAttendant.getPump());
        savedAttendant.get().setFuelTypes(updatedAttendant.getFuelTypes());
        pumpAttendantRepository.save(savedAttendant.get());
        return "Pump attendant updated successfully";
    }

    public String deletePumpAttendant(UUID id) {
        Optional<PumpAttendant> attendant = pumpAttendantRepository.findById(id);
        if (attendant.isEmpty()) {
            return "The pump attendant with that id does not exist";
        }

        pumpAttendantRepository.delete(attendant.get());
        return "Pump attendant deleted successfully";
    }
}
