package com.example.dailyflow.backend.backend.controllers;

import com.example.dailyflow.backend.backend.services.DashboardService;
import com.example.dailyflow.backend.backend.models.dto.DashboardSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummaryDTO getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }
}
