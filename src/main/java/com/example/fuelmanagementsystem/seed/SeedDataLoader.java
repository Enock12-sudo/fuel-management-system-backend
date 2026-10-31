package com.example.fuelmanagementsystem.seed;

import com.example.fuelmanagementsystem.entity.Driver;
import com.example.fuelmanagementsystem.entity.FuelIssue;
import com.example.fuelmanagementsystem.entity.FuelIssueStatus;
import com.example.fuelmanagementsystem.entity.FuelReceipt;
import com.example.fuelmanagementsystem.entity.FuelType;
import com.example.fuelmanagementsystem.entity.Pump;
import com.example.fuelmanagementsystem.entity.PumpAttendant;
import com.example.fuelmanagementsystem.repository.DriverRepository;
import com.example.fuelmanagementsystem.repository.FuelIssueRepository;
import com.example.fuelmanagementsystem.repository.FuelReceiptRepository;
import com.example.fuelmanagementsystem.repository.FuelTypeRepository;
import com.example.fuelmanagementsystem.repository.PumpAttendantRepository;
import com.example.fuelmanagementsystem.repository.PumpRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SeedDataLoader implements CommandLineRunner {

    private final FuelTypeRepository fuelTypeRepository;
    private final PumpRepository pumpRepository;
    private final PumpAttendantRepository pumpAttendantRepository;
    private final DriverRepository driverRepository;
    private final FuelIssueRepository fuelIssueRepository;
    private final FuelReceiptRepository fuelReceiptRepository;

    public SeedDataLoader(
            FuelTypeRepository fuelTypeRepository,
            PumpRepository pumpRepository,
            PumpAttendantRepository pumpAttendantRepository,
            DriverRepository driverRepository,
            FuelIssueRepository fuelIssueRepository,
            FuelReceiptRepository fuelReceiptRepository
    ) {
        this.fuelTypeRepository = fuelTypeRepository;
        this.pumpRepository = pumpRepository;
        this.pumpAttendantRepository = pumpAttendantRepository;
        this.driverRepository = driverRepository;
        this.fuelIssueRepository = fuelIssueRepository;
        this.fuelReceiptRepository = fuelReceiptRepository;
    }

    @Override
    public void run(String... args) {
        if (hasExistingData()) {
            return;
        }

        FuelType petrol = saveFuelType("Petrol");
        FuelType diesel = saveFuelType("Diesel");
        FuelType kerosene = saveFuelType("Kerosene");
        saveFuelType("LPG");

        Pump pump1 = savePump(1, "Pump 1");
        Pump pump2 = savePump(2, "Pump 2");
        Pump pump3 = savePump(3, "Pump 3");
        savePump(4, "Pump 4");

        PumpAttendant attendant1 = saveAttendant("Aline", "Mugisha", "0788000001", pump1, petrol, diesel);
        PumpAttendant attendant2 = saveAttendant("Eric", "Niyonzima", "0788000002", pump2, diesel);
        PumpAttendant attendant3 = saveAttendant("Beata", "Uwase", "0788000003", pump3, petrol, kerosene);

        Driver driver1 = saveDriver("Jean", "Habimana", "DL-2021-04567", "0788110001");
        Driver driver2 = saveDriver("Alice", "Habimana", "DL-2022-01111", "0788110002");
        Driver driver3 = saveDriver("Claude", "Nkurunziza", "DL-2023-02222", "0788110003");
        Driver driver4 = saveDriver("Diane", "Mukamana", "DL-2024-03333", "0788110004");
        Driver driver5 = saveDriver("Patrick", "Rukundo", "DL-2025-04444", "0788110005");

        List<FuelIssue> issues = new ArrayList<>();
        issues.add(saveIssue("2026-10-20", FuelIssueStatus.REQUESTED, "RAA 100A", 20, driver1, attendant1));
        issues.add(saveIssue("2026-10-20", FuelIssueStatus.DECLINED, "RAA 200B", 15, driver2, attendant1));
        issues.add(saveIssue("2026-10-22", FuelIssueStatus.DISPENSED, "RAA 300C", 40, driver1, attendant2));
        issues.add(saveIssue("2026-10-25", FuelIssueStatus.APPROVED, "RAA 400D", 25, driver1, attendant3));
        issues.add(saveIssue("2026-10-27", FuelIssueStatus.DISPENSED, "RAA 500E", 30, driver2, attendant2));
        issues.add(saveIssue("2026-10-29", FuelIssueStatus.REQUESTED, "RAA 600F", 18, driver3, attendant1));
        issues.add(saveIssue("2026-11-02", FuelIssueStatus.APPROVED, "RAA 700G", 22, driver3, attendant3));
        issues.add(saveIssue("2026-11-05", FuelIssueStatus.DISPENSED, "RAA 800H", 35, driver4, attendant2));
        issues.add(saveIssue("2026-11-08", FuelIssueStatus.DECLINED, "RAA 900J", 12, driver5, attendant1));
        issues.add(saveIssue("2026-11-12", FuelIssueStatus.REQUESTED, "RAA 111K", 28, driver2, attendant3));

        saveReceipt(issues.get(2), "Fleet card", "Full tank", 45000);
        saveReceipt(issues.get(2), "Cash", "Top up", 5000);
        saveReceipt(issues.get(4), "Fleet card", "Diesel purchase", 38000);
        saveReceipt(issues.get(7), "Mobile money", "Fuel purchase", 42000);
    }

    private boolean hasExistingData() {
        return fuelTypeRepository.count() > 0
                || pumpRepository.count() > 0
                || pumpAttendantRepository.count() > 0
                || driverRepository.count() > 0
                || fuelIssueRepository.count() > 0
                || fuelReceiptRepository.count() > 0;
    }

    private FuelType saveFuelType(String name) {
        FuelType fuelType = new FuelType();
        fuelType.setName(name);
        return fuelTypeRepository.save(fuelType);
    }

    private Pump savePump(int pumpNumber, String name) {
        Pump pump = new Pump();
        pump.setPumpNumber(pumpNumber);
        pump.setName(name);
        return pumpRepository.save(pump);
    }

    private PumpAttendant saveAttendant(
            String firstName,
            String lastName,
            String phone,
            Pump pump,
            FuelType... fuelTypes
    ) {
        PumpAttendant attendant = new PumpAttendant();
        attendant.setFirstName(firstName);
        attendant.setLastName(lastName);
        attendant.setPhone(phone);
        attendant.setPump(pump);
        attendant.setFuelTypes(List.of(fuelTypes));
        return pumpAttendantRepository.save(attendant);
    }

    private Driver saveDriver(String firstName, String lastName, String licenseNumber, String phone) {
        Driver driver = new Driver();
        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setLicenseNumber(licenseNumber);
        driver.setPhone(phone);
        return driverRepository.save(driver);
    }

    private FuelIssue saveIssue(
            String issueDate,
            FuelIssueStatus status,
            String plateNumber,
            double litres,
            Driver driver,
            PumpAttendant attendant
    ) {
        FuelIssue fuelIssue = new FuelIssue();
        fuelIssue.setIssueDate(LocalDate.parse(issueDate));
        fuelIssue.setStatus(status);
        fuelIssue.setPlateNumber(plateNumber);
        fuelIssue.setLitres(litres);
        fuelIssue.setDriver(driver);
        fuelIssue.setAttendant(attendant);
        return fuelIssueRepository.save(fuelIssue);
    }

    private void saveReceipt(FuelIssue fuelIssue, String paymentMethod, String remarks, int amountPaid) {
        FuelReceipt fuelReceipt = new FuelReceipt();
        fuelReceipt.setPaymentMethod(paymentMethod);
        fuelReceipt.setRemarks(remarks);
        fuelReceipt.setAmountPaid(amountPaid);
        fuelReceipt.setReceiptDate(LocalDate.now());
        fuelReceipt.setFuelIssue(fuelIssue);
        fuelReceiptRepository.save(fuelReceipt);
    }
}
