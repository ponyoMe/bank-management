package com.example.bank_management.controller;


import com.example.bank_management.service.PdfReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bank_management.model.Transaction;
import com.example.bank_management.service.ReportService;
import com.example.bank_management.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final CustomerService customerService;
    private final PdfReportService pdfReportService;

    public ReportController(ReportService reportService, CustomerService customerService, PdfReportService pdfReportService) {
        this.reportService = reportService;
        this.customerService = customerService;
        this.pdfReportService=pdfReportService;
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactionHistory(Authentication authentication) {
        Long customerId = customerService.findCustomerByEmail(authentication.getName()).getId();
        return reportService.getTransactionForCustomer(customerId);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdfReport(Authentication authentication) {
        Long customerId = customerService.findCustomerByEmail(authentication.getName()).getId();
        List<Transaction> transactions = reportService.getTransactionForCustomer(customerId);
        byte[] pdfBytes = pdfReportService.generatePdfReport(transactions);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

