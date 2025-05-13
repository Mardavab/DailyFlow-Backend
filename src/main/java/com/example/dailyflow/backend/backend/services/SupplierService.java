package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import com.example.dailyflow.backend.backend.models.entities.Supplier;

public interface SupplierService {

    List<Supplier> findAllSuppliers();

    Optional<Supplier> findSupplierById(Long id);

    Supplier saveSupplier(Supplier supplier);

    Optional<Supplier> updateSupplier(Supplier supplier, Long id);

    void removeSupplier(Long id);
}
