package com.example.dailyflow.backend.backend.controllers;

import com.example.dailyflow.backend.backend.models.entities.Product;
import com.example.dailyflow.backend.backend.services.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Zapatos");
        product1.setPrice(100L);
        product1.setStock(10L);
        product1.setSize("M");

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Camisa");
        product2.setPrice(50L);
        product2.setStock(20L);
        product2.setSize("L");
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.findAllProducts()).thenReturn(products);

        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Zapatos"))
                .andExpect(jsonPath("$[1].name").value("Camisa"));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.findProductById(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zapatos"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.findProductById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/product/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        Product createdProduct = new Product();
        createdProduct.setId(3L);
        createdProduct.setName("Gorra");
        createdProduct.setPrice(30L);
        createdProduct.setStock(5L);
        createdProduct.setSize("S");

        when(productService.saveProduct(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3,\"name\":\"Gorra\",\"price\":30,\"stock\":5,\"size\":\"S\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Gorra"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Zapatos nuevos");
        updatedProduct.setPrice(120L);
        updatedProduct.setStock(8L);
        updatedProduct.setSize("M");

        when(productService.updateProduct(any(Product.class), eq(1L)))
                .thenReturn(Optional.of(updatedProduct));

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Zapatos nuevos\",\"price\":120,\"stock\":8,\"size\":\"M\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Zapatos nuevos"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(productService.findProductById(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(delete("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        when(productService.findProductById(88L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/product/88")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
