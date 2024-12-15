package com.example.backend.utils.converter;

import com.example.backend.models.Message;
import com.example.backend.utils.dtos.MessageDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class MessageConverterDto implements Converter<Message, MessageDto> {
    @Override
    public Message createFromDto(MessageDto messageDto){
        return Message.builder()
                .chatId(messageDto.chatId())
                .content(messageDto.content())
                .sentDate(LocalDate.now())
                .sentTime(LocalTime.now())
                .sender(messageDto.sender())
                .build();
    }

    @Override
    public MessageDto createFromEntity(Message message){
        return new MessageDto(message.getChatId(), message.getContent(), message.getSender());
    }
}
