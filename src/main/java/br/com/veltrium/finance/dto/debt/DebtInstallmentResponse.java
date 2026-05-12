package br.com.veltrium.finance.dto.debt;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DebtInstallmentResponse(
        Long id,
        Integer number,
        BigDecimal amount,
        LocalDate dueDate,
        boolean paid,
        LocalDate paidAt
) {}
