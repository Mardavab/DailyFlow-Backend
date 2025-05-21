package com.example.dailyflow.backend.backend.models.dto;

import java.time.LocalDate;

public class PaymentDTO {
    private LocalDate date;
    private Double amount;
    private String method;

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
}
