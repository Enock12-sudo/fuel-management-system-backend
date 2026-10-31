package com.example.fuelmanagementsystem.controller;

import com.example.fuelmanagementsystem.entity.Pump;
import com.example.fuelmanagementsystem.service.PumpService;
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
@RequestMapping("/api/pumps")
public class PumpController {

    private final PumpService pumpService;

    public PumpController(PumpService pumpService) {
        this.pumpService = pumpService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePump(@RequestBody Pump pump) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pumpService.savePump(pump));
    }

    @GetMapping("/all")
    public List<Pump> getAllPumps() {
        return pumpService.getAllPumps();
    }

    @GetMapping("/free")
    public List<Pump> getFreePumps() {
        return pumpService.getFreePumps();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPumpById(@PathVariable UUID id) {
        Optional<Pump> pump = pumpService.getPumpById(id);
        if (pump.isPresent()) {
            return ResponseEntity.ok(pump.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The pump with that id does not exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePump(@PathVariable UUID id, @RequestBody Pump pump) {
        String message = pumpService.updatePump(id, pump);
        if (message.equals("The pump with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePump(@PathVariable UUID id) {
        String message = pumpService.deletePump(id);
        if (message.equals("The pump with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
