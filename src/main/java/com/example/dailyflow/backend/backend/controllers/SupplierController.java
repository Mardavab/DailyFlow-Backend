package com.example.dailyflow.backend.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.dailyflow.backend.backend.models.entities.Supplier;
import com.example.dailyflow.backend.backend.services.SupplierService;


@RestController
@RequestMapping("/suppliers")
public class SupplierController {

     @Autowired
    private SupplierService service;

    @GetMapping
    public List<Supplier> list(){
        return service.findAllSuppliers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Supplier> supplierOptional = service.findSupplierById(id);
        if (supplierOptional.isPresent()) {
            return ResponseEntity.ok(supplierOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier create(@RequestBody Supplier supplier){
        return service.saveSupplier(supplier);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Supplier supplier) {
        Optional<Supplier> o = service.updateSupplier(supplier,id);
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){ 
        Optional<Supplier> o = service.findSupplierById(id);
        if (o.isPresent()) {
            service.removeSupplier(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}
