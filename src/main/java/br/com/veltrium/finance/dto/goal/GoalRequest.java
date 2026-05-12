package br.com.veltrium.finance.dto.goal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalRequest(
        @NotNull Long householdId,
        @NotBlank String name,
        @NotNull @DecimalMin("0.01") BigDecimal targetAmount,
        BigDecimal currentAmount,
        LocalDate deadline
) {}
