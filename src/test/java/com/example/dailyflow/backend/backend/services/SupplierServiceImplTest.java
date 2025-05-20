package com.example.dailyflow.backend.backend.services;

import com.example.dailyflow.backend.backend.models.entities.Supplier;
import com.example.dailyflow.backend.backend.repositories.SupplierRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Proveedor Uno");
        supplier.setContactPerson("Laura Díaz");
        supplier.setPhone("1234567890");
        supplier.setEmail("laura@proveedor.com");
        supplier.setAddress("Calle 10 #5-20");
    }

    @Test
    void testFindAllSuppliers() {
        when(supplierRepository.findAll()).thenReturn(Collections.singletonList(supplier));
        assertEquals(1, supplierService.findAllSuppliers().size());
    }

    @Test
    void testFindSupplierById() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        Optional<Supplier> result = supplierService.findSupplierById(1L);
        assertTrue(result.isPresent());
        assertEquals("Proveedor Uno", result.get().getName());
    }

    @Test
    void testSaveSupplier() {
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        Supplier saved = supplierService.saveSupplier(supplier);
        assertEquals("Proveedor Uno", saved.getName());
    }

    @Test
    void testUpdateSupplier_Found() {
        Supplier newData = new Supplier();
        newData.setName("Proveedor Actualizado");
        newData.setContactPerson("Carlos Ruiz");
        newData.setPhone("9999999999");
        newData.setEmail("carlos@proveedor.com");
        newData.setAddress("Av. Siempre Viva 123");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Supplier> updated = supplierService.updateSupplier(newData, 1L);
        assertTrue(updated.isPresent());
        assertEquals("Proveedor Actualizado", updated.get().getName());
        assertEquals("Carlos Ruiz", updated.get().getContactPerson());
        assertEquals("9999999999", updated.get().getPhone());
        assertEquals("carlos@proveedor.com", updated.get().getEmail());
        assertEquals("Av. Siempre Viva 123", updated.get().getAddress());
    }

    @Test
    void testUpdateSupplier_NotFound() {
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Supplier> result = supplierService.updateSupplier(supplier, 999L);
        assertFalse(result.isPresent());
    }

    @Test
    void testRemoveSupplier() {
        doNothing().when(supplierRepository).deleteById(1L);
        supplierService.removeSupplier(1L);
        verify(supplierRepository, times(1)).deleteById(1L);
    }
}
