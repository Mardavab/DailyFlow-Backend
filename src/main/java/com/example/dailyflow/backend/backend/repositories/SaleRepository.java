package com.example.dailyflow.backend.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dailyflow.backend.backend.models.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}
