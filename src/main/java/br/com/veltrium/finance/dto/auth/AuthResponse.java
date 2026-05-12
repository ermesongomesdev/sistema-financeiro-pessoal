package br.com.veltrium.finance.dto.auth;

public record AuthResponse(
        String token,
        Long userId,
        String name,
        String email
) {}
