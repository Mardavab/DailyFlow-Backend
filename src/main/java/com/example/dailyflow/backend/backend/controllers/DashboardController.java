package com.example.dailyflow.backend.backend.controllers;

import com.example.dailyflow.backend.backend.services.DashboardService;
import com.example.dailyflow.backend.backend.models.dto.DashboardSummaryDTO;
import com.example.dailyflow.backend.backend.repositories.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final ReportService reportService;

    public DashboardController(DashboardService dashboardService, ReportService reportService) {
        this.dashboardService = dashboardService;
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public DashboardSummaryDTO getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }

    @GetMapping("/daily")
    public ResponseEntity<byte[]> dailyReport(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        try {
            if (date == null) {
                date = LocalDate.now();
            }
            
            System.out.println("=== Generando reporte para fecha: " + date);
            
            byte[] pdf = reportService.generateDailyReportPdf(date);
            
            System.out.println("=== PDF generado. Tamaño: " + pdf.length + " bytes");
            
            if (pdf.length == 0) {
                System.err.println("ERROR: PDF vacío generado");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"Resumen_" + date.toString() + ".pdf\"");
            headers.setContentLength(pdf.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);
                    
        } catch (Exception e) {
            System.err.println("ERROR al generar PDF: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}