package com.example.fuelmanagementsystem.service;

import com.example.fuelmanagementsystem.entity.Pump;
import com.example.fuelmanagementsystem.repository.DriverRepository;
import com.example.fuelmanagementsystem.repository.FuelIssueRepository;
import com.example.fuelmanagementsystem.repository.FuelReceiptRepository;
import com.example.fuelmanagementsystem.repository.PumpAttendantRepository;
import com.example.fuelmanagementsystem.repository.PumpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final DriverRepository driverRepository;
    private final PumpAttendantRepository pumpAttendantRepository;
    private final PumpRepository pumpRepository;
    private final FuelIssueRepository fuelIssueRepository;
    private final FuelReceiptRepository fuelReceiptRepository;

    public DashboardService(
            DriverRepository driverRepository,
            PumpAttendantRepository pumpAttendantRepository,
            PumpRepository pumpRepository,
            FuelIssueRepository fuelIssueRepository,
            FuelReceiptRepository fuelReceiptRepository
    ) {
        this.driverRepository = driverRepository;
        this.pumpAttendantRepository = pumpAttendantRepository;
        this.pumpRepository = pumpRepository;
        this.fuelIssueRepository = fuelIssueRepository;
        this.fuelReceiptRepository = fuelReceiptRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        long freePumpCount = 0;
        for (Pump pump : pumpRepository.findFreePumps()) {
            freePumpCount++;
        }

        summary.put("drivers", driverRepository.count());
        summary.put("attendants", pumpAttendantRepository.count());
        summary.put("freePumps", freePumpCount);
        summary.put("issuesToday", fuelIssueRepository.countByIssueDate(LocalDate.now()));
        summary.put("requested", fuelIssueRepository.countByStatus(
                com.example.fuelmanagementsystem.entity.FuelIssueStatus.REQUESTED
        ));
        summary.put("receipts", fuelReceiptRepository.count());
        return summary;
    }
}
