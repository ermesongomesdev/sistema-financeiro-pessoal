package br.com.veltrium.finance.dto.transaction;

import br.com.veltrium.finance.model.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull Long householdId,
        @NotNull Long categoryId,
        @NotBlank String description,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull TransactionType type,
        @NotNull LocalDate dueDate,
        boolean paid
) {}
