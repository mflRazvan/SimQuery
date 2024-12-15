package com.example.backend.utils.converter;

import com.example.backend.models.Chat;
import com.example.backend.utils.dtos.ChatDto;

import java.util.ArrayList;

public class ChatConverterDto implements Converter<Chat, ChatDto> {
    @Override
    public Chat createFromDto(ChatDto chatDto){
        return Chat.builder()
                .title(chatDto.title())
                .messages(new ArrayList<>())
                .build();
    }

    @Override
    public ChatDto createFromEntity(Chat chat){
        return new ChatDto(chat.getId(), chat.getTitle());
    }
}
