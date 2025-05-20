package com.example.dailyflow.backend.backend.models.dto;

import java.util.List;

public class SaleDTO {

    private String paymentMethod;
    private List<SaleDetailDTO> items;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<SaleDetailDTO> getItems() {
        return items;
    }

    public void setItems(List<SaleDetailDTO> items) {
        this.items = items;
    }
}