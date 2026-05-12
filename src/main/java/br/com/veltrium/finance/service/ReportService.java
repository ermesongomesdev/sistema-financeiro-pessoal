package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.report.MonthlyReportResponse;
import br.com.veltrium.finance.model.TransactionEntry;
import br.com.veltrium.finance.model.enums.TransactionType;
import br.com.veltrium.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final TransactionRepository transactionRepository;
    private final HouseholdService householdService;
    private final TransactionService transactionService;

    public ReportService(TransactionRepository transactionRepository, HouseholdService householdService, TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.householdService = householdService;
        this.transactionService = transactionService;
    }

    public MonthlyReportResponse monthly(Long householdId, int year, int month) {
        householdService.assertMember(householdId);
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        List<TransactionEntry> entries = transactionRepository.findByHouseholdIdAndDueDateBetweenOrderByDueDateAsc(householdId, start, end);

        BigDecimal income = sum(entries, TransactionType.INCOME);
        BigDecimal expense = sum(entries, TransactionType.EXPENSE);
        Map<String, BigDecimal> expenseByCategory = groupByCategory(entries, TransactionType.EXPENSE);
        Map<String, BigDecimal> incomeByCategory = groupByCategory(entries, TransactionType.INCOME);

        return new MonthlyReportResponse(
                householdId,
                year,
                month,
                income,
                expense,
                income.subtract(expense),
                expenseByCategory,
                incomeByCategory,
                entries.stream().map(transactionService::toResponse).toList()
        );
    }

    private BigDecimal sum(List<TransactionEntry> entries, TransactionType type) {
        return entries.stream()
                .filter(t -> t.getType() == type)
                .map(TransactionEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, BigDecimal> groupByCategory(List<TransactionEntry> entries, TransactionType type) {
        return entries.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, TransactionEntry::getAmount, BigDecimal::add)
                ));
    }
}
