package com.example.backend.controllers;

import com.example.backend.models.Message;
import com.example.backend.services.MessageService;
import com.example.backend.utils.converter.MessageConverterDto;
import com.example.backend.utils.dtos.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {
    @Autowired
    private MessageService messageService;

    private final MessageConverterDto dtoConverter = new MessageConverterDto();

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Message>> getAllMessages() {
        try {
            return new ResponseEntity<>(messageService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving messages");
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Message> getMessageById(@PathVariable int id) {
        try {
            Message message = messageService.getById(id);
            if (message == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
            }
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving message");
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Mono<String>> addMessage(@RequestBody MessageDto messageDto) {
        try {
            Message message = dtoConverter.createFromDto(messageDto);
            return new ResponseEntity<>(messageService.add(message), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating message");
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) {
        try {
            if (message.getId() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message ID is required for update");
            }
            messageService.update(message);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating message");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_BASIC_USER', 'ROLE_PREMIUM_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        try {
            messageService.delete(id);
            return new ResponseEntity<>("Message deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting message");
        }
    }
}