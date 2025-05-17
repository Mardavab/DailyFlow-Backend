package com.example.dailyflow.backend.backend.models.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private String status; // pendiente, pagada, parcial, vencida

    @Column(name = "pending_balance", nullable = false)
    private Double pendingBalance;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    // Métodos para calcular el saldo pendiente
    @PrePersist
    @PreUpdate
    private void calculatePendingBalance() {
        double paidAmount = payments.stream()
            .mapToDouble(Payment::getAmount)
            .sum();
        this.pendingBalance = total - paidAmount;
        
        // Actualizar estado basado en el saldo
        if (pendingBalance <= 0) {
            status = "pagada";
        } else if (LocalDate.now().isAfter(dueDate)) {
            status = "vencida";
        } else if (paidAmount > 0) {
            status = "parcial";
        } else {
            status = "pendiente";
        }
    }

    // Constructores, getters y setters
    public Invoice() {
    }

    // Getters y Setters para todos los campos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(Double pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    // Métodos helper para manejar pagos
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInvoice(this);
        calculatePendingBalance();
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setInvoice(null);
        calculatePendingBalance();
    }
}