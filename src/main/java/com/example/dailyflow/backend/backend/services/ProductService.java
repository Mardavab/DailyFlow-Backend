package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import com.example.dailyflow.backend.backend.models.entities.Product;

public interface ProductService {
    List<Product> findAllProducts();

    Optional<Product> findProductById(Long id);

    Product saveProduct(Product product);

    Optional<Product> updateProduct(Product product, Long id);

    void removeProduct(Long id);
}
