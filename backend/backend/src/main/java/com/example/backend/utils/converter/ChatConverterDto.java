package com.example.backend.utils.converter;

import com.example.backend.models.Chat;
import com.example.backend.utils.dtos.ChatDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ChatConverterDto implements Converter<Chat, ChatDto> {
    @Override
    public Chat createFromDto(ChatDto dto) {
        return Chat.builder()
                .title(dto.title())
                .messages(new ArrayList<>())
                .build();
    }

    @Override
    public ChatDto createFromEntity(Chat entity) {
        return new ChatDto(
                entity.getExternalId(),
                entity.getTitle(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}