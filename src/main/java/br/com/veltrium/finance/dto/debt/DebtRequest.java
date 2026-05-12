package br.com.veltrium.finance.dto.debt;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DebtRequest(
        @NotNull Long householdId,
        @NotBlank String description,
        @NotBlank String creditor,
        @NotNull @DecimalMin("0.01") BigDecimal totalAmount,
        @NotNull @Min(1) Integer installments,
        @NotNull LocalDate firstDueDate
) {}
