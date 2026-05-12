package br.com.veltrium.finance.dto.goal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalResponse(
        Long id,
        String name,
        BigDecimal targetAmount,
        BigDecimal currentAmount,
        BigDecimal progressPercent,
        LocalDate deadline
) {}
