package com.example.dailyflow.backend.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.dailyflow.backend.backend.models.entities.Expense;

public interface ExpenseRespository extends CrudRepository<Expense,Long> {

}
