package com.example.backend.utils.dtos;

public record ChatDto (
    Long id,
    String title
) {
    public ChatDto(Long id){
        this(id, "New chat");
    }
}
