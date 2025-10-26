package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dailyflow.backend.backend.models.entities.Product;
import com.example.dailyflow.backend.backend.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return (List<Product>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProductById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void removeProduct(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    @Override
    @Transactional
    public Optional<Product> updateProduct(Product product, Long id) {
        Optional<Product> optionalProduct = findProductById(id);

        if (optionalProduct.isPresent()) {
            Product productDb = optionalProduct.get();

            productDb.setCode(product.getCode());
            productDb.setBrand(product.getBrand());
            productDb.setName(product.getName());
            productDb.setPurchasePrice(product.getPurchasePrice());
            productDb.setSalePrice(product.getSalePrice());
            productDb.setStock(product.getStock());
            productDb.setSize(product.getSize());

            return Optional.of(this.saveProduct(productDb));
        }

        return Optional.empty();
    }
}
