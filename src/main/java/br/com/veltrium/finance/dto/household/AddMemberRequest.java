package br.com.veltrium.finance.dto.household;

import br.com.veltrium.finance.model.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(
        @Email @NotBlank String email,
        @NotNull MemberRole role
) {}
