package com.example.backend.utils.converter;

import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import com.example.backend.services.ChatService;
import com.example.backend.utils.dtos.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConverterDto implements Converter<Message, MessageDto> {
    private final ChatService chatService;

    @Override
    public Message createFromDto(MessageDto dto) {
        Chat chat = chatService.getChatByExternalId(dto.chatExternalId());

        return Message.builder()
                .externalId(dto.externalId())
                .chat(chat)
                .content(dto.content())
                .sender(dto.sender())
                .sentAt(dto.sentAt() != null ? dto.sentAt() : null)
                .build();
    }

    @Override
    public MessageDto createFromEntity(Message entity) {
        return new MessageDto(
                entity.getExternalId(),
                entity.getChat().getExternalId(),
                entity.getContent(),
                entity.getSentAt(),
                entity.getSender()
        );
    }
}