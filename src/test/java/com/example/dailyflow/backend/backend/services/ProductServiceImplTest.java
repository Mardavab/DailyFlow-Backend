package com.example.dailyflow.backend.backend.services;

import com.example.dailyflow.backend.backend.models.entities.Product;
import com.example.dailyflow.backend.backend.repositories.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(1500000L);
        product.setStock(10L);
        product.setSize("15 inch");
    }

    @Test
    void testFindAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        assertEquals(1, productService.findAllProducts().size());
    }

    @Test
    void testFindProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> result = productService.findProductById(1L);
        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product saved = productService.saveProduct(product);
        assertEquals("Laptop", saved.getName());
    }

    @Test
    void testUpdateProduct_Found() {
        Product newData = new Product();
        newData.setName("Laptop Pro");
        newData.setPrice(2000000L);
        newData.setStock(5L);
        newData.setSize("17 inch");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Product> updated = productService.updateProduct(newData, 1L);
        assertTrue(updated.isPresent());
        assertEquals("Laptop Pro", updated.get().getName());
        assertEquals(2000000L, updated.get().getPrice());
        assertEquals(5L, updated.get().getStock());
        assertEquals("17 inch", updated.get().getSize());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Product> result = productService.updateProduct(product, 999L);
        assertFalse(result.isPresent());
    }

    @Test
    void testRemoveProduct() {
        doNothing().when(productRepository).deleteById(1L);
        productService.removeProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
