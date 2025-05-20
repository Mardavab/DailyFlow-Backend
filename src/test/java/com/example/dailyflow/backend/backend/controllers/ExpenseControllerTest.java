package com.example.dailyflow.backend.backend.controllers;

import com.example.dailyflow.backend.backend.models.entities.Expense;
import com.example.dailyflow.backend.backend.services.ExpenseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    void setUp() {
        expense1 = new Expense();
        expense1.setId(1L);
        expense1.setAmount(1000L);
        expense1.setDescription("Publicidad mensual");

        expense2 = new Expense();
        expense2.setId(2L);
        expense2.setAmount(2000L);
        expense2.setDescription("Materiales operativos");
    }

    @Test
    void testGetAllExpenses() throws Exception {
        List<Expense> expenses = Arrays.asList(expense1, expense2);
        when(expenseService.findAllExpenses()).thenReturn(expenses);

        mockMvc.perform(get("/expense")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Publicidad mensual"))
                .andExpect(jsonPath("$[1].description").value("Materiales operativos"));
    }

    @Test
    void testGetExpenseById() throws Exception {
        when(expenseService.findExpenseById(1L)).thenReturn(Optional.of(expense1));

        mockMvc.perform(get("/expense/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Publicidad mensual"));
    }

    @Test
    void testGetExpenseById_NotFound() throws Exception {
        when(expenseService.findExpenseById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/expense/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateExpense() throws Exception {
        Expense newExpense = new Expense();
        newExpense.setId(3L);
        newExpense.setAmount(5000L);
        newExpense.setDescription("Nuevo gasto");

        when(expenseService.saveExpense(any(Expense.class))).thenReturn(newExpense);

        mockMvc.perform(post("/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3,\"amount\":5000,\"description\":\"Nuevo gasto\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Nuevo gasto"));
    }

    @Test
    void testUpdateExpense() throws Exception {
        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setAmount(1500L);
        updatedExpense.setDescription("Publicidad actualizada");

        when(expenseService.updateExpense(any(Expense.class), eq(1L)))
                .thenReturn(Optional.of(updatedExpense));

        mockMvc.perform(put("/expense/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1500,\"description\":\"Publicidad actualizada\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Publicidad actualizada"));
    }

    @Test
    void testDeleteExpense() throws Exception {
        when(expenseService.findExpenseById(1L)).thenReturn(Optional.of(expense1));

        mockMvc.perform(delete("/expense/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteExpense_NotFound() throws Exception {
        when(expenseService.findExpenseById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/expense/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
