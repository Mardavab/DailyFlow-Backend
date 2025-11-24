package com.example.dailyflow.backend.backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.dailyflow.backend.backend.models.entities.Expense;

public interface ExpenseRespository extends CrudRepository<Expense, Long> {

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.date = :date")
    Double sumAmountByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM Expense e WHERE e.date = :date")
    Long countByDate(@Param("date") LocalDate date);

    @Query("SELECT e FROM Expense e WHERE e.date = :date")
    List<Expense> findByDate(@Param("date") LocalDate date);
}
