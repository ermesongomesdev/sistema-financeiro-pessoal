package br.com.veltrium.finance.controller;

import br.com.veltrium.finance.service.ExportService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exports")
public class ExportController {
    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/monthly/pdf")
    public ResponseEntity<byte[]> monthlyPdf(@RequestParam Long householdId, @RequestParam int year, @RequestParam int month) {
        byte[] content = exportService.monthlyPdf(householdId, year, month);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("relatorio-financeiro-" + year + "-" + month + ".pdf")
                        .build().toString())
                .body(content);
    }

    @GetMapping("/monthly/excel")
    public ResponseEntity<byte[]> monthlyExcel(@RequestParam Long householdId, @RequestParam int year, @RequestParam int month) {
        byte[] content = exportService.monthlyExcel(householdId, year, month);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("relatorio-financeiro-" + year + "-" + month + ".xlsx")
                        .build().toString())
                .body(content);
    }
}
