package com.example.backend.controllers;

import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import com.example.backend.services.ChatService;
import com.example.backend.utils.dtos.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<Chat>> getAllChats() {
        return ResponseEntity.ok(chatService.getAllChats());
    }

    @GetMapping("/{externalId}")
    public ResponseEntity<Chat> getChatByExternalId(@PathVariable UUID externalId) {
        return ResponseEntity.ok(chatService.getChatByExternalId(externalId));
    }

    @GetMapping("/{externalId}/messages")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable UUID externalId) {
        return ResponseEntity.ok(chatService.getChatMessages(externalId));
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody ChatDto chatDto) {
        return ResponseEntity.ok(chatService.createChat(chatDto));
    }

    @PutMapping("/{externalId}")
    public ResponseEntity<Chat> updateChat(
            @PathVariable UUID externalId,
            @RequestBody ChatDto chatDto) {
        return ResponseEntity.ok(chatService.updateChat(externalId, chatDto));
    }

    @DeleteMapping("/{externalId}")
    public ResponseEntity<Void> deleteChat(@PathVariable UUID externalId) {
        chatService.deleteChat(externalId);
        return ResponseEntity.noContent().build();
    }
}