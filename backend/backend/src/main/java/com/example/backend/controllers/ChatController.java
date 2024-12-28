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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {
    @Autowired
    private ChatService chatService;

    private final ChatConverterDto dtoConverter = new ChatConverterDto();
    private final MessageConverterDto messageDtoConverter = new MessageConverterDto();

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ChatDto>> getAllChats() {
        try {
            List<ChatDto> result = new ArrayList<>();
            List<Chat> chats = chatService.getAll();
            for (Chat chat : chats) {
                result.add(dtoConverter.createFromEntity(chat));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving chats");
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ChatDto> getById(@PathVariable int id) {
        try {
            Chat chat = chatService.getById(id);
            if (chat == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
            }
            return new ResponseEntity<>(dtoConverter.createFromEntity(chat), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving chat");
        }
    }

    @GetMapping("{id}/messages")
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<MessageDto>> getChatsMessages(@PathVariable int id) {
        try {
            List<MessageDto> result = new ArrayList<>();
            List<Message> messages = chatService.getChatsMessages(id);
            for (Message message : messages) {
                result.add(messageDtoConverter.createFromEntity(message));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving chat messages");
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ChatDto> updateChat(@RequestBody ChatDto chatDto) {
        try {
            if (chatDto.id() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chat ID is required for update");
            }
            chatService.update(dtoConverter.createFromDto(chatDto));
            return new ResponseEntity<>(chatDto, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating chat");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> deleteChat(@PathVariable int id) {
        try {
            chatService.delete(id);
            return new ResponseEntity<>("Chat deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting chat");
        }
    }
}