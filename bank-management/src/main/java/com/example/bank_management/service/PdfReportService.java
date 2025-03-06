package com.example.bank_management.service;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.example.bank_management.model.Transaction;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfReportService {

    public byte[] generatePdfReport(List<Transaction> transactions) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Account Statement"));
            document.add(new Paragraph(" "));

            for (Transaction t : transactions) {
                document.add(new Paragraph(t.getCreatedAt() + " - " + t.getAmount() + " - " +
                        (t.getToAccount() != null ? "To: " + t.getToAccount().getId() : "Withdrawal")));
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}

