package br.com.veltrium.finance.controller;

import br.com.veltrium.finance.dto.report.MonthlyReportResponse;
import br.com.veltrium.finance.service.ReportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly")
    public MonthlyReportResponse monthly(@RequestParam Long householdId, @RequestParam int year, @RequestParam int month) {
        return reportService.monthly(householdId, year, month);
    }
}
