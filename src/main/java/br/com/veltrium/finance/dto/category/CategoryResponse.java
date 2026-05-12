package br.com.veltrium.finance.dto.category;

import br.com.veltrium.finance.model.enums.TransactionType;

public record CategoryResponse(
        Long id,
        String name,
        TransactionType type,
        String color
) {}
