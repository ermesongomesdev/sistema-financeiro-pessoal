package br.com.veltrium.finance.dto.goal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record GoalDepositRequest(
        @NotNull @DecimalMin("0.01") BigDecimal amount
) {}
