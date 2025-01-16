package com.example.backend.utils.dtos;

import com.example.backend.models.SenderType;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDto(
        MessageDetails userMessage,
        MessageDetails aiMessage
) {
    public record MessageDetails(
            UUID externalId,
            String content,
            SenderType sender,
            LocalDateTime sentAt
    ) {}
}