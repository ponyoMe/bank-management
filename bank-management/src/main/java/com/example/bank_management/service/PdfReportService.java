package com.example.bank_management.service;

import com.example.bank_management.model.Customer;
import com.example.bank_management.repository.CustomerRepository;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.example.bank_management.model.Transaction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PdfReportService {

    private final CustomerRepository customerRepository;

    public PdfReportService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public byte[] generatePdfReport(List<Transaction> transactions) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Account Statement"));
            document.add(new Paragraph(" "));

            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            Optional<Customer> customer = customerRepository.findByEmail(username);

            for (Transaction t : transactions) {
                document.add(new Paragraph(getTransactionDescription(t, customer.get())));
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public String getTransactionDescription(Transaction transaction, Customer customer) {
        StringBuilder description = new StringBuilder();

        if (transaction.getFromAccount().getCustomer().getId().equals(transaction.getToAccount().getCustomer().getId())) {
            // Internal transfer
            description.append("Internal Transfer: ")
                    .append(transaction.getFromAccount().getName())
                    .append(" ➝ ")
                    .append(transaction.getToAccount().getName());
        } else {
            // External transfer
            if (transaction.getFromAccount().getCustomer().getId().equals(customer.getId())) {
                description.append("Sent to: ")
                        .append(transaction.getToAccount().getCustomer().getName());
            } else if (transaction.getToAccount().getCustomer().getId().equals(customer.getId())) {
                description.append("Received from: ")
                        .append(transaction.getFromAccount().getCustomer().getName());
            }
        }

        description.append(" - ").append(transaction.getAmount()).append(" $");

        return description.toString();
    }

}

