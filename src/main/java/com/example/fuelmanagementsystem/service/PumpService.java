package com.example.fuelmanagementsystem.service;

import com.example.fuelmanagementsystem.entity.Pump;
import com.example.fuelmanagementsystem.repository.PumpRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PumpService {

    private final PumpRepository pumpRepository;

    public PumpService(PumpRepository pumpRepository) {
        this.pumpRepository = pumpRepository;
    }

    public String savePump(Pump pump) {
        pumpRepository.save(pump);
        return "Pump saved successfully";
    }

    public List<Pump> getAllPumps() {
        return pumpRepository.findAll();
    }

    public Optional<Pump> getPumpById(UUID id) {
        return pumpRepository.findById(id);
    }

    public List<Pump> getFreePumps() {
        return pumpRepository.findFreePumps();
    }

    public Optional<Pump> getPumpByNumber(int pumpNumber) {
        return pumpRepository.findByPumpNumber(pumpNumber);
    }

    public String updatePump(UUID id, Pump updatedPump) {
        Optional<Pump> savedPump = pumpRepository.findById(id);
        if (savedPump.isEmpty()) {
            return "The pump with that id does not exist";
        }

        savedPump.get().setPumpNumber(updatedPump.getPumpNumber());
        savedPump.get().setName(updatedPump.getName());
        pumpRepository.save(savedPump.get());
        return "Pump updated successfully";
    }

    public String deletePump(UUID id) {
        Optional<Pump> pump = pumpRepository.findById(id);
        if (pump.isEmpty()) {
            return "The pump with that id does not exist";
        }

        pumpRepository.delete(pump.get());
        return "Pump deleted successfully";
    }
}
