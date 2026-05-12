package br.com.veltrium.finance.dto.transaction;

import br.com.veltrium.finance.model.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        String description,
        BigDecimal amount,
        TransactionType type,
        LocalDate dueDate,
        boolean paid,
        LocalDate paidAt,
        Long categoryId,
        String categoryName
) {}
