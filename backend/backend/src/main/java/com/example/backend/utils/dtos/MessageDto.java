package com.example.backend.utils.dtos;

public record MessageDto (
    Long id,
    Long chatId,
    String content,
    String sender
) {
    public MessageDto(Long id, String content, String sender){
        this(id, (long) -1, content, sender);
    }
}
