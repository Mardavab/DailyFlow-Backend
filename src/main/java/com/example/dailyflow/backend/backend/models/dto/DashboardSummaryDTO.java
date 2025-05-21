package com.example.dailyflow.backend.backend.models.dto;

import java.util.List;
import java.util.Map;

public class DashboardSummaryDTO {
    private double monthlyIncome;
    private double annualIncome;
    private double tasksProgress; // entre 0 y 1
    private int pendingRequests;
    private List<Double> incomeSummary; // Ejemplo: 12 valores, uno por mes
    private Map<String, Double> incomeSources; // "Directos": 10000, "Sociales": ...

    // Getters y Setters
    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public double getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(double annualIncome) { this.annualIncome = annualIncome; }

    public double getTasksProgress() { return tasksProgress; }
    public void setTasksProgress(double tasksProgress) { this.tasksProgress = tasksProgress; }

    public int getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(int pendingRequests) { this.pendingRequests = pendingRequests; }

    public List<Double> getIncomeSummary() { return incomeSummary; }
    public void setIncomeSummary(List<Double> incomeSummary) { this.incomeSummary = incomeSummary; }

    public Map<String, Double> getIncomeSources() { return incomeSources; }
    public void setIncomeSources(Map<String, Double> incomeSources) { this.incomeSources = incomeSources; }
}
