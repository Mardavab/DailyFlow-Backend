package com.example.dailyflow.backend.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.dailyflow.backend.backend.models.entities.Product;

public interface ProductRepository extends CrudRepository<Product,Long> {

}
