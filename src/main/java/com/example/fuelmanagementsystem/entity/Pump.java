package com.example.fuelmanagementsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "pump")
public class Pump {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int pumpNumber;

    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPumpNumber() {
        return pumpNumber;
    }

    public void setPumpNumber(int pumpNumber) {
        this.pumpNumber = pumpNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
