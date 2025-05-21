package com.example.dailyflow.backend.backend.repositories;

import com.example.dailyflow.backend.backend.models.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {}
