package com.example.dailyflow.backend.backend.repositories;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.example.dailyflow.backend.backend.models.entities.Expense;
import com.example.dailyflow.backend.backend.models.entities.Sale;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final SaleRepository saleRepository;
    private final ExpenseRespository expenseRepository;

    public ReportService(SaleRepository saleRepository, ExpenseRespository expenseRepository) {
        this.saleRepository = saleRepository;
        this.expenseRepository = expenseRepository;
    }

    public byte[] generateDailyReportPdf(LocalDate date) throws IOException {
        System.out.println(">>> Iniciando generación de PDF para: " + date);
        
        // 1) Obtener datos
        Double totalSales = saleRepository.sumTotalByDate(date);
        if (totalSales == null) totalSales = 0.0;
        System.out.println(">>> Total ventas: " + totalSales);

        Long countSales = saleRepository.countByDate(date);
        List<Sale> sales = saleRepository.findByDate(date);
        System.out.println(">>> Cantidad de ventas: " + countSales);

        Double totalExpenses = expenseRepository.sumAmountByDate(date);
        if (totalExpenses == null) totalExpenses = 0.0;

        Long countExpenses = expenseRepository.countByDate(date);
        List<Expense> expenses = expenseRepository.findByDate(date);

        Double net = totalSales - totalExpenses;

        // 2) Crear PDF simple con PDFBox
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);
        float y = 750;
        float leftMargin = 50;

        try {
            // Título
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 18);
            content.newLineAtOffset(leftMargin, y);
            String title = "Resumen diario - " + date.format(DateTimeFormatter.ISO_DATE);
            content.showText(title);
            content.endText();
            y -= 30;

            // Totales
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(leftMargin, y);
            content.showText(String.format("Ventas totales: $%.2f", totalSales));
            content.endText();
            y -= 18;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(leftMargin, y);
            content.showText(String.format("Numero de ventas: %d", countSales));
            content.endText();
            y -= 18;

            content.beginText();
            content.newLineAtOffset(leftMargin, y);
            content.showText(String.format("Gastos totales: $%.2f", totalExpenses));
            content.endText();
            y -= 18;

            content.beginText();
            content.newLineAtOffset(leftMargin, y);
            content.showText(String.format("Numero de gastos: %d", countExpenses));
            content.endText();
            y -= 18;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.newLineAtOffset(leftMargin, y);
            content.showText(String.format("Neto (ventas - gastos): $%.2f", net));
            content.endText();
            y -= 24;

            // Lista breve de ventas
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 13);
            content.newLineAtOffset(leftMargin, y);
            content.showText("Detalle de ventas:");
            content.endText();
            y -= 18;

            content.setFont(PDType1Font.HELVETICA, 11);
            for (Sale s : sales) {
                if (y < 80) { // nueva página si es necesario
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = 750;
                    content.setFont(PDType1Font.HELVETICA, 11);
                }
                content.beginText();
                content.newLineAtOffset(leftMargin, y);
                content.showText(String.format("Id: %d  - Total: $%.2f", s.getId(), s.getAmount()));
                content.endText();
                y -= 14;
            }
            y -= 10;

            // Lista breve de gastos
            if (y < 100) {
                content.close();
                page = new PDPage();
                document.addPage(page);
                content = new PDPageContentStream(document, page);
                y = 750;
            }
            
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 13);
            content.newLineAtOffset(leftMargin, y);
            content.showText("Detalle de gastos:");
            content.endText();
            y -= 18;

            content.setFont(PDType1Font.HELVETICA, 11);
            for (Expense e : expenses) {
                if (y < 80) {
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = 750;
                    content.setFont(PDType1Font.HELVETICA, 11);
                }
                content.beginText();
                content.newLineAtOffset(leftMargin, y);
                content.showText(String.format("Id: %d  - Monto: $%.2f  - %s", 
                    e.getId(), e.getAmount(), e.getDescription()));
                content.endText();
                y -= 14;
            }

        } finally {
            // IMPORTANTE: Siempre cerrar el content stream antes de guardar
            content.close();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return baos.toByteArray();
    }
}