package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.report.MonthlyReportResponse;
import br.com.veltrium.finance.dto.transaction.TransactionResponse;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class ExportService {
    private final ReportService reportService;
    private final NumberFormat money = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    public ExportService(ReportService reportService) {
        this.reportService = reportService;
    }

    public byte[] monthlyPdf(Long householdId, int year, int month) {
        MonthlyReportResponse report = reportService.monthly(householdId, year, month);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("Relatório Financeiro Mensal", titleFont));
            document.add(new Paragraph("Período: " + month + "/" + year));
            document.add(new Paragraph("Receitas: " + money(report.totalIncome())));
            document.add(new Paragraph("Despesas: " + money(report.totalExpense())));
            document.add(new Paragraph("Saldo: " + money(report.balance())));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            addHeader(table, "Data");
            addHeader(table, "Descrição");
            addHeader(table, "Categoria");
            addHeader(table, "Tipo");
            addHeader(table, "Valor");

            for (TransactionResponse t : report.transactions()) {
                table.addCell(t.dueDate().toString());
                table.addCell(t.description());
                table.addCell(t.categoryName());
                table.addCell(t.type().name());
                table.addCell(money(t.amount()));
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    public byte[] monthlyExcel(Long householdId, int year, int month) {
        MonthlyReportResponse report = reportService.monthly(householdId, year, month);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet summary = workbook.createSheet("Resumo");
            addSummary(summary, 0, "Ano", BigDecimal.valueOf(year));
            addSummary(summary, 1, "Mês", BigDecimal.valueOf(month));
            addSummary(summary, 2, "Receitas", report.totalIncome());
            addSummary(summary, 3, "Despesas", report.totalExpense());
            addSummary(summary, 4, "Saldo", report.balance());

            Sheet transactions = workbook.createSheet("Transações");
            Row header = transactions.createRow(0);
            header.createCell(0).setCellValue("Data");
            header.createCell(1).setCellValue("Descrição");
            header.createCell(2).setCellValue("Categoria");
            header.createCell(3).setCellValue("Tipo");
            header.createCell(4).setCellValue("Pago");
            header.createCell(5).setCellValue("Valor");

            int rowIndex = 1;
            for (TransactionResponse t : report.transactions()) {
                Row row = transactions.createRow(rowIndex++);
                row.createCell(0).setCellValue(t.dueDate().toString());
                row.createCell(1).setCellValue(t.description());
                row.createCell(2).setCellValue(t.categoryName());
                row.createCell(3).setCellValue(t.type().name());
                row.createCell(4).setCellValue(t.paid() ? "Sim" : "Não");
                row.createCell(5).setCellValue(t.amount().doubleValue());
            }

            for (int i = 0; i <= 5; i++) transactions.autoSizeColumn(i);
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Excel", e);
        }
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 11, Font.BOLD)));
        table.addCell(cell);
    }

    private void addSummary(Sheet sheet, int rowIndex, String label, BigDecimal value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value.doubleValue());
    }

    private String money(BigDecimal value) {
        return money.format(value);
    }
}
