package com.example.dailyflow.backend.backend.services;

import com.example.dailyflow.backend.backend.models.entities.Expense;
import com.example.dailyflow.backend.backend.repositories.ExpenseRespository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseServiceImplTest {

    @Mock
    private ExpenseRespository expenseRespository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense expense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        expense = new Expense();
        expense.setId(1L);
        expense.setAmount(5000L);
        expense.setDescription("Gasto operativo");
    }

    @Test
    void testFindAllExpenses() {
        when(expenseRespository.findAll()).thenReturn(Collections.singletonList(expense));
        assertEquals(1, expenseService.findAllExpenses().size());
    }

    @Test
    void testFindExpenseById() {
        when(expenseRespository.findById(1L)).thenReturn(Optional.of(expense));
        Optional<Expense> result = expenseService.findExpenseById(1L);
        assertTrue(result.isPresent());
        assertEquals("Gasto operativo", result.get().getDescription());
    }

    @Test
    void testSaveExpense() {
        when(expenseRespository.save(any(Expense.class))).thenReturn(expense);
        Expense saved = expenseService.saveExpense(expense);
        assertEquals("Gasto operativo", saved.getDescription());
    }

    @Test
    void testUpdateExpense_Found() {
        Expense updatedData = new Expense();
        updatedData.setAmount(8000L);
        updatedData.setDescription("Gasto actualizado");

        when(expenseRespository.findById(1L)).thenReturn(Optional.of(expense));
        when(expenseRespository.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Expense> updated = expenseService.updateExpense(updatedData, 1L);
        assertTrue(updated.isPresent());
        assertEquals(8000L, updated.get().getAmount());
        assertEquals("Gasto actualizado", updated.get().getDescription());
    }

    @Test
    void testUpdateExpense_NotFound() {
        when(expenseRespository.findById(99L)).thenReturn(Optional.empty());
        Optional<Expense> result = expenseService.updateExpense(expense, 99L);
        assertFalse(result.isPresent());
    }

    @Test
    void testRemoveExpense() {
        doNothing().when(expenseRespository).deleteById(1L);
        expenseService.removeExpense(1L);
        verify(expenseRespository, times(1)).deleteById(1L);
    }
}
