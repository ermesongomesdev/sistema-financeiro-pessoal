package br.com.veltrium.finance.dto.household;

import java.util.List;

public record HouseholdResponse(
        Long id,
        String name,
        List<MemberResponse> members
) {}
