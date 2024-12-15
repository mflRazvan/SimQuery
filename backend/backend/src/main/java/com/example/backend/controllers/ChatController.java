package com.example.backend.controllers;

import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import com.example.backend.services.ChatService;
import com.example.backend.utils.converter.ChatConverterDto;
import com.example.backend.utils.converter.MessageConverterDto;
import com.example.backend.utils.dtos.ChatDto;
import com.example.backend.utils.dtos.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    private ChatService chatService;

    private final ChatConverterDto dtoConverter = new ChatConverterDto();
    private final MessageConverterDto messageDtoConverter = new MessageConverterDto();

    @GetMapping
    public ResponseEntity<List<ChatDto>> getAllChats() {
        List<ChatDto> result = new ArrayList<>();
        List<Chat> chats = chatService.getAll();
        for (Chat chat : chats) {
            result.add(dtoConverter.createFromEntity(chat));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ChatDto> getById(@PathVariable int id) {
        return new ResponseEntity<>(dtoConverter.createFromEntity(chatService.getById(id)), HttpStatus.OK);
    }

    @GetMapping("{id}/messages")
    public ResponseEntity<List<MessageDto>> getChatsMessages(@PathVariable int id) {
        List<MessageDto> result = new ArrayList<>();
        List<Message> messages = chatService.getChatsMessages(id);
        for (Message message : messages) {
            result.add(messageDtoConverter.createFromEntity(message));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*@PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        chatService.add(chat);
        return new ResponseEntity<>(chat, HttpStatus.CREATED);
    }*/

    @PutMapping
    public ResponseEntity<ChatDto> updateChat(@RequestBody ChatDto chatDto) {
        chatService.update(dtoConverter.createFromDto(chatDto));
        return new ResponseEntity<>(chatDto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteChat(@PathVariable int id) {
        chatService.delete(id);
        return new ResponseEntity<>("Record deleted successfully", HttpStatus.OK);
    }
}
