package com.example.dailyflow.backend.backend.controllers;

import com.example.dailyflow.backend.backend.models.dto.InvoiceDTO;
import com.example.dailyflow.backend.backend.models.dto.PaymentDTO;
import com.example.dailyflow.backend.backend.models.entities.Invoice;
import com.example.dailyflow.backend.backend.models.entities.Payment;
import com.example.dailyflow.backend.backend.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody InvoiceDTO dto) {
        return invoiceService.createInvoice(dto);
    }

    @PutMapping("/{id}")
    public Invoice updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO dto) {
        return invoiceService.updateInvoice(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }

    @PostMapping("/{id}/payments")
    public Payment addPayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        return invoiceService.addPayment(id, paymentDTO);
    }

    @GetMapping("/{id}/payments")
    public List<Payment> getPayments(@PathVariable Long id) {
        return invoiceService.getPayments(id);
    }
}
