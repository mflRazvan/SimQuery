package com.example.backend.utils.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatDto(
        UUID externalId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ChatDto(String title) {
        this(null, title, null, null);
    }
}