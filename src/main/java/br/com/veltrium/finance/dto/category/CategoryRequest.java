package br.com.veltrium.finance.dto.category;

import br.com.veltrium.finance.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotNull Long householdId,
        @NotBlank String name,
        @NotNull TransactionType type,
        String color
) {}
