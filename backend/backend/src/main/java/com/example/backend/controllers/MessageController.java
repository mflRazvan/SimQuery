package com.example.backend.controllers;

import com.example.backend.models.Message;
import com.example.backend.services.MessageService;
import com.example.backend.utils.converter.MessageConverterDto;
import com.example.backend.utils.dtos.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    private final MessageConverterDto dtoConverter = new MessageConverterDto();

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return new ResponseEntity<>(messageService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id) {
        return new ResponseEntity<>(messageService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Mono<String>> addMessage(@RequestBody MessageDto messageDto) {
        Message message = dtoConverter.createFromDto(messageDto);
        return new ResponseEntity<>(messageService.add(message), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) {
        messageService.update(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        messageService.delete(id);
        return new ResponseEntity<>("Record deleted successfully", HttpStatus.OK);
    }
}
