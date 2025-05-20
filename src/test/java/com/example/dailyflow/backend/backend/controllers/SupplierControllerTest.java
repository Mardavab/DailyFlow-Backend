package com.example.dailyflow.backend.backend.controllers;

import com.example.dailyflow.backend.backend.models.entities.Supplier;
import com.example.dailyflow.backend.backend.services.SupplierService;

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

@WebMvcTest(SupplierController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    private Supplier supplier1;
    private Supplier supplier2;

    @BeforeEach
    void setUp() {
        supplier1 = new Supplier();
        supplier1.setId(1L);
        supplier1.setName("Proveedor A");
        supplier1.setContactPerson("Juan Perez");
        supplier1.setPhone("1234567890");
        supplier1.setEmail("juan@proveedor.com");
        supplier1.setAddress("Calle 123");

        supplier2 = new Supplier();
        supplier2.setId(2L);
        supplier2.setName("Proveedor B");
        supplier2.setContactPerson("Ana Gomez");
        supplier2.setPhone("0987654321");
        supplier2.setEmail("ana@proveedor.com");
        supplier2.setAddress("Carrera 45");
    }

    @Test
    void testGetAllSuppliers() throws Exception {
        List<Supplier> suppliers = Arrays.asList(supplier1, supplier2);
        when(supplierService.findAllSuppliers()).thenReturn(suppliers);

        mockMvc.perform(get("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Proveedor A"))
                .andExpect(jsonPath("$[1].name").value("Proveedor B"));
    }

    @Test
    void testGetSupplierById() throws Exception {
        when(supplierService.findSupplierById(1L)).thenReturn(Optional.of(supplier1));

        mockMvc.perform(get("/suppliers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Proveedor A"));
    }

    @Test
    void testGetSupplierById_NotFound() throws Exception {
        when(supplierService.findSupplierById(100L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/suppliers/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSupplier() throws Exception {
        Supplier newSupplier = new Supplier();
        newSupplier.setId(3L);
        newSupplier.setName("Proveedor C");
        newSupplier.setContactPerson("Carlos Ruiz");
        newSupplier.setPhone("1112223333");
        newSupplier.setEmail("carlos@proveedor.com");
        newSupplier.setAddress("Avenida Siempre Viva");

        when(supplierService.saveSupplier(any(Supplier.class))).thenReturn(newSupplier);

        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3,\"name\":\"Proveedor C\",\"contactPerson\":\"Carlos Ruiz\",\"phone\":\"1112223333\",\"email\":\"carlos@proveedor.com\",\"address\":\"Avenida Siempre Viva\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Proveedor C"));
    }

    @Test
    void testUpdateSupplier() throws Exception {
        Supplier updated = new Supplier();
        updated.setId(1L);
        updated.setName("Proveedor A actualizado");
        updated.setContactPerson("Juan Pérez");
        updated.setPhone("9999999999");
        updated.setEmail("juan_actualizado@proveedor.com");
        updated.setAddress("Calle 456");

        when(supplierService.updateSupplier(any(Supplier.class), eq(1L)))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/suppliers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Proveedor A actualizado\",\"contactPerson\":\"Juan Pérez\",\"phone\":\"9999999999\",\"email\":\"juan_actualizado@proveedor.com\",\"address\":\"Calle 456\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Proveedor A actualizado"));
    }

    @Test
    void testDeleteSupplier() throws Exception {
        when(supplierService.findSupplierById(1L)).thenReturn(Optional.of(supplier1));

        mockMvc.perform(delete("/suppliers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSupplier_NotFound() throws Exception {
        when(supplierService.findSupplierById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/suppliers/404")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
