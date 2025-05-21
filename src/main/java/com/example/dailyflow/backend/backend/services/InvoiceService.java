package com.example.dailyflow.backend.backend.services;

import com.example.dailyflow.backend.backend.models.dto.InvoiceDTO;
import com.example.dailyflow.backend.backend.models.dto.InvoiceItemDTO;
import com.example.dailyflow.backend.backend.models.dto.PaymentDTO;
import com.example.dailyflow.backend.backend.models.entities.*;
import com.example.dailyflow.backend.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.LocalDate;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    // Crear factura
    public Invoice createInvoice(InvoiceDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setIssueDate(dto.getIssueDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setStatus("PENDIENTE");
        invoice.setTotal(0.0);
        invoice.setPendingBalance(0.0);

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        invoice.setSupplier(supplier);

        List<InvoiceItem> items = new ArrayList<>();
        double total = 0.0;
        for (InvoiceItemDTO itemDTO : dto.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemDTO.getDescription());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setSubtotal(itemDTO.getPrice() * itemDTO.getQuantity());
            item.setInvoice(invoice);

            items.add(item);
            total += item.getSubtotal();
        }
        invoice.setItems(items);
        invoice.setTotal(total);
        invoice.setPendingBalance(total);

        return invoiceRepository.save(invoice);
    }

    // Listar facturas
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // Buscar factura por ID
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }

    // Actualizar factura
    public Invoice updateInvoice(Long id, InvoiceDTO dto) {
        Invoice invoice = getInvoiceById(id);
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setIssueDate(dto.getIssueDate());
        invoice.setDueDate(dto.getDueDate());
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        invoice.setSupplier(supplier);

        // Actualizar items
        invoice.getItems().clear();
        double total = 0.0;
        List<InvoiceItem> items = new ArrayList<>();
        for (InvoiceItemDTO itemDTO : dto.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemDTO.getDescription());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setSubtotal(itemDTO.getPrice() * itemDTO.getQuantity());
            item.setInvoice(invoice);

            items.add(item);
            total += item.getSubtotal();
        }
        invoice.setItems(items);
        invoice.setTotal(total);

        // Si ya tiene pagos, recalcula saldo y estado
        double sumPagos = invoice.getPayments().stream().mapToDouble(Payment::getAmount).sum();
        invoice.setPendingBalance(total - sumPagos);
        if (invoice.getPendingBalance() <= 0.01) {
            invoice.setStatus("PAGADO");
            invoice.setPendingBalance(0.0);
        } else if (sumPagos > 0) {
            invoice.setStatus("PARCIAL");
        } else {
            invoice.setStatus("PENDIENTE");
        }

        return invoiceRepository.save(invoice);
    }

    // Eliminar factura
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    // Registrar pago (¡Aquí está el fix!)
    public Payment addPayment(Long invoiceId, PaymentDTO paymentDTO) {
        Invoice invoice = getInvoiceById(invoiceId);
        Payment payment = new Payment();

        // Siempre asigna la fecha aquí:
        if (paymentDTO.getDate() != null) {
            payment.setDate(paymentDTO.getDate());
        } else {
            payment.setDate(LocalDate.now());
        }

        payment.setAmount(paymentDTO.getAmount());
        payment.setMethod(paymentDTO.getMethod());
        payment.setInvoice(invoice);

        paymentRepository.save(payment);

        // Recalcular saldo y estado
        double totalPagos = invoice.getPayments().stream().mapToDouble(Payment::getAmount).sum() + paymentDTO.getAmount();
        double nuevoSaldo = invoice.getTotal() - totalPagos;
        invoice.setPendingBalance(Math.max(nuevoSaldo, 0.0));

        if (invoice.getPendingBalance() <= 0.01) {
            invoice.setStatus("PAGADO");
            invoice.setPendingBalance(0.0);
        } else if (totalPagos > 0) {
            invoice.setStatus("PARCIAL");
        } else {
            invoice.setStatus("PENDIENTE");
        }

        invoiceRepository.save(invoice);
        return payment;
    }

    // Ver pagos de una factura
    public List<Payment> getPayments(Long invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);
        return invoice.getPayments();
    }
}
