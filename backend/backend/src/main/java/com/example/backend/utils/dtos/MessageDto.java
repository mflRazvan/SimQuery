package com.example.backend.utils.dtos;

import com.example.backend.models.SenderType;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageDto(
        UUID externalId,
        UUID chatExternalId,
        String content,
        LocalDateTime sentAt,
        SenderType sender
) {
    public MessageDto(UUID chatExternalId, String content, SenderType sender) {
        this(null, chatExternalId, content, null, sender);
    }
}