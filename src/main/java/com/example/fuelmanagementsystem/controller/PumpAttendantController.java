package com.example.fuelmanagementsystem.controller;

import com.example.fuelmanagementsystem.entity.PumpAttendant;
import com.example.fuelmanagementsystem.service.PumpAttendantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/pump-attendants")
public class PumpAttendantController {

    private final PumpAttendantService pumpAttendantService;

    public PumpAttendantController(PumpAttendantService pumpAttendantService) {
        this.pumpAttendantService = pumpAttendantService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePumpAttendant(@RequestBody PumpAttendant pumpAttendant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pumpAttendantService.savePumpAttendant(pumpAttendant));
    }

    @GetMapping("/all")
    public List<PumpAttendant> getAllPumpAttendants() {
        return pumpAttendantService.getAllPumpAttendants();
    }

    @GetMapping("/by-fuel-type")
    public List<PumpAttendant> getPumpAttendantsByFuelType(@RequestParam String name) {
        return pumpAttendantService.getPumpAttendantsByFuelType(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPumpAttendantById(@PathVariable UUID id) {
        Optional<PumpAttendant> pumpAttendant = pumpAttendantService.getPumpAttendantById(id);
        if (pumpAttendant.isPresent()) {
            return ResponseEntity.ok(pumpAttendant.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The pump attendant with that id does not exist");
        }
    }

    @PutMapping("/{pumpAttendantId}/fuel-types/{fuelTypeId}")
    public ResponseEntity<String> addFuelType(
            @PathVariable UUID pumpAttendantId,
            @PathVariable UUID fuelTypeId
    ) {
        String message = pumpAttendantService.addFuelType(pumpAttendantId, fuelTypeId);
        if (message.equals("The pump attendant with that id does not exist")
                || message.equals("That fuel type does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("The pump attendant already has that fuel type")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @PatchMapping("/{pumpAttendantId}/pump")
    public ResponseEntity<String> changePump(
            @PathVariable UUID pumpAttendantId,
            @RequestParam int pumpNumber
    ) {
        String message = pumpAttendantService.changePump(pumpAttendantId, pumpNumber);
        if (message.equals("The pump attendant with that id does not exist")
                || message.equals("The pump with that pumpNumber does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("That pump is taken by another pump attendant")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePumpAttendant(
            @PathVariable UUID id,
            @RequestBody PumpAttendant pumpAttendant
    ) {
        String message = pumpAttendantService.updatePumpAttendant(id, pumpAttendant);
        if (message.equals("The pump attendant with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePumpAttendant(@PathVariable UUID id) {
        String message = pumpAttendantService.deletePumpAttendant(id);
        if (message.equals("The pump attendant with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
