package com.example.dailyflow.backend.backend.services;

import com.example.dailyflow.backend.backend.models.dto.DashboardSummaryDTO;
import com.example.dailyflow.backend.backend.repositories.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private SaleRepository saleRepository;

    public DashboardSummaryDTO getDashboardSummary() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        double monthlyIncome = Optional.ofNullable(saleRepository.sumAmountByMonth(year, month)).orElse(0.0);
        double annualIncome = Optional.ofNullable(saleRepository.sumAmountByYear(year)).orElse(0.0);
        double tasksProgress = 0.5; // Simulado

        int pendingRequests = 0; // Simulado

        // Procesamiento de ingresos mensuales
        List<Object[]> summaryRaw = saleRepository.findMonthlyIncomeSummary(year);
        List<Double> incomeSummary = new ArrayList<>(Collections.nCopies(12, 0.0));
        for (Object[] row : summaryRaw) {
            int mes = ((Number) row[0]).intValue();
            double monto = ((Number) row[1]).doubleValue();
            incomeSummary.set(mes - 1, monto);
        }

        // Procesamiento de fuentes de ingreso
        List<Object[]> sourcesRaw = saleRepository.sumAmountBySource(year);
        Map<String, Double> sources = new HashMap<>();
        for (Object[] row : sourcesRaw) {
            String fuente = (String) row[0];
            double monto = ((Number) row[1]).doubleValue();
            if (fuente == null) {
                fuente = "Sin fuente"; // O usa otro texto como "Otro" o "No especificado"
            }
            sources.put(fuente, monto);
        }

        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        dto.setMonthlyIncome(monthlyIncome);
        dto.setAnnualIncome(annualIncome);
        dto.setTasksProgress(tasksProgress);
        dto.setPendingRequests(pendingRequests);
        dto.setIncomeSummary(incomeSummary);
        dto.setIncomeSources(sources);

        return dto;
    }
}
