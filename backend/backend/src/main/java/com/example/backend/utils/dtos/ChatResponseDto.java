package com.example.backend.utils.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatResponseDto(
        UUID externalId,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
