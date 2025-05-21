package com.example.dailyflow.backend.backend.models.dto;

import java.time.LocalDate;
import java.util.List;

public class InvoiceDTO {
    private String invoiceNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private Long supplierId;
    private List<InvoiceItemDTO> items;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public List<InvoiceItemDTO> getItems() {
        return items;
    }
    public void setItems(List<InvoiceItemDTO> items) {
        this.items = items;
    }
}
