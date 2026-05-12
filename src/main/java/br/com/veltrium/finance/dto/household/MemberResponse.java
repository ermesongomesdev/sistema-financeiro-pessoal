package br.com.veltrium.finance.dto.household;

import br.com.veltrium.finance.model.enums.MemberRole;

public record MemberResponse(
        Long userId,
        String name,
        String email,
        MemberRole role
) {}
