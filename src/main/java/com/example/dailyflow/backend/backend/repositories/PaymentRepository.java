package com.example.dailyflow.backend.backend.repositories;

import com.example.dailyflow.backend.backend.models.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
