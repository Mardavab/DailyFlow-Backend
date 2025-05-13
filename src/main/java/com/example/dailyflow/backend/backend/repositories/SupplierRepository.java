package com.example.dailyflow.backend.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.dailyflow.backend.backend.models.entities.Supplier;

public interface SupplierRepository extends CrudRepository<Supplier,Long> {

}
