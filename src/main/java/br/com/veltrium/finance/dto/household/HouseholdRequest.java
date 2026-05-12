package br.com.veltrium.finance.dto.household;

import jakarta.validation.constraints.NotBlank;

public record HouseholdRequest(@NotBlank String name) {}
