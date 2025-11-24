package com.example.dailyflow.backend.backend.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dailyflow.backend.backend.models.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Ingresos mensuales (mes actual de un año)
    @Query("SELECT COALESCE(SUM(s.amount),0) FROM Sale s WHERE YEAR(s.date) = :year AND MONTH(s.date) = :month")
    Double sumAmountByMonth(@Param("year") int year, @Param("month") int month);

    // Ingresos anuales (todo el año)
    @Query("SELECT COALESCE(SUM(s.amount),0) FROM Sale s WHERE YEAR(s.date) = :year")
    Double sumAmountByYear(@Param("year") int year);

    // Resumen mensual: suma de ingresos por cada mes del año (devuelve lista con 12
    // valores, puede tener nulls)
    @Query("SELECT MONTH(s.date), COALESCE(SUM(s.amount),0) FROM Sale s WHERE YEAR(s.date) = :year GROUP BY MONTH(s.date) ORDER BY MONTH(s.date)")
    List<Object[]> findMonthlyIncomeSummary(@Param("year") int year);

    // Fuentes de ingresos: suma agrupada por fuente
    @Query("SELECT s.source, COALESCE(SUM(s.amount),0) FROM Sale s WHERE YEAR(s.date) = :year GROUP BY s.source")
    List<Object[]> sumAmountBySource(@Param("year") int year);

    @Query("SELECT SUM(s.amount) FROM Sale s WHERE s.date = :date")
    Double sumTotalByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.date = :date")
    Long countByDate(@Param("date") LocalDate date);

    @Query("SELECT s FROM Sale s WHERE s.date = :date")
    List<Sale> findByDate(@Param("date") LocalDate date);

}
