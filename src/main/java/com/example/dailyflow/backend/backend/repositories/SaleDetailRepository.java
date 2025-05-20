package com.example.dailyflow.backend.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dailyflow.backend.backend.models.entities.SaleDetail;

public interface SaleDetailRepository extends JpaRepository<SaleDetail,Long> {

}
