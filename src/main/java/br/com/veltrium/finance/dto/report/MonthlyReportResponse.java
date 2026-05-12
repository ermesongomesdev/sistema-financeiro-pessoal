package br.com.veltrium.finance.dto.report;

import br.com.veltrium.finance.dto.transaction.TransactionResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record MonthlyReportResponse(
        Long householdId,
        int year,
        int month,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        Map<String, BigDecimal> expenseByCategory,
        Map<String, BigDecimal> incomeByCategory,
        List<TransactionResponse> transactions
) {}
