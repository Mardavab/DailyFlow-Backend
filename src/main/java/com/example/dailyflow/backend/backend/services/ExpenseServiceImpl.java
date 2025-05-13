package com.example.dailyflow.backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dailyflow.backend.backend.models.entities.Expense;
import com.example.dailyflow.backend.backend.repositories.ExpenseRespository;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRespository repository;

    @Override    
    @Transactional(readOnly = true)
    public List<Expense> findAllExpenses() {
        return (List<Expense>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Expense> findExpenseById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void removeExpense(Long id) {
        repository.deleteById(id);

    }

    @Override
    @Transactional
    public Expense saveExpense(Expense expense) {
        return repository.save(expense);
    }

    @Override
    public Optional<Expense> updateExpense(Expense expense, Long id){
        Optional<Expense> o = findExpenseById(id);
        if (o.isPresent()) {
            Expense expenseDb = o.orElseThrow();
            expenseDb.setAmount((expense.getAmount()));
            expenseDb.setDescription(expense.getDescription());
            return Optional.of(this.saveExpense(expenseDb));
        }
        return Optional.empty();
    }
}
