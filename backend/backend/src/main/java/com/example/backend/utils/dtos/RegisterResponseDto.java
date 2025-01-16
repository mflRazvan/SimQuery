package com.example.backend.utils.dtos;

public record RegisterResponseDto(
        String message,
        boolean requiresEmailVerification
) {
    public RegisterResponseDto(String message) {
        this(message, true);
    }
}