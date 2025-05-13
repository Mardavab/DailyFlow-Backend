package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import com.example.dailyflow.backend.backend.models.entities.Expense;

public interface ExpenseService {

    List<Expense> findAllExpenses();

    Optional<Expense> findExpenseById(Long id);

    Expense saveExpense(Expense expense);

    Optional<Expense> updateExpense(Expense expense, Long id);

    void removeExpense(Long id);

}
