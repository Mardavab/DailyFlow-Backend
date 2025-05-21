package com.example.dailyflow.backend.backend.repositories;

import com.example.dailyflow.backend.backend.models.entities.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {}
