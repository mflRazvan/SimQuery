package com.example.backend.controllers;

import com.example.backend.models.Message;
import com.example.backend.models.SenderType;
import com.example.backend.services.MessageService;
import com.example.backend.utils.dtos.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/{externalId}")
    public ResponseEntity<Message> getMessageByExternalId(@PathVariable UUID externalId) {
        return ResponseEntity.ok(messageService.getMessageByExternalId(externalId));
    }

    @PostMapping
    public ResponseEntity<MessageResponseDto> createMessage(@RequestBody MessageRequest request) {
        log.debug("Received message request: {}", request);
        MessageResponseDto response = messageService.createMessage(
                request.chatExternalId(),
                request.content(),
                SenderType.USER
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID externalId) {
        messageService.deleteMessage(externalId);
        return ResponseEntity.noContent().build();
    }

    public record MessageRequest(UUID chatExternalId, String content) {}
}