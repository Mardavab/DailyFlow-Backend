package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dailyflow.backend.backend.models.entities.Supplier;
import com.example.dailyflow.backend.backend.repositories.SupplierRepository;

@Service
public class SupplierServiceImpl implements SupplierService{

    @Autowired
    private SupplierRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findAllSuppliers() {
        return (List<Supplier>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findSupplierById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void removeSupplier(Long id) {
        repository.deleteById(id);

    }

    @Override
    @Transactional
    public Supplier saveSupplier(Supplier supplier) {
        return repository.save(supplier);
    }

    @Override
    public Optional<Supplier> updateSupplier(Supplier supplier, Long id){
        Optional<Supplier> o = findSupplierById(id);
        if (o.isPresent()) {
            Supplier supplierDb = o.orElseThrow();
            supplierDb.setName((supplier.getName()));
            supplierDb.setContactPerson((supplier.getContactPerson()));
            supplierDb.setPhone((supplier.getPhone()));
            supplierDb.setEmail(supplier.getEmail());
            supplierDb.setAddress(supplier.getAddress());
            return Optional.of(this.saveSupplier(supplierDb));
        }
        return Optional.empty();
    }
}
