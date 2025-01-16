package com.example.backend.services;

import com.example.backend.exceptions.ChatNotFoundException;
import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import com.example.backend.models.User;
import com.example.backend.repositories.ChatRepository;
import com.example.backend.repositories.MessageRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utils.dtos.ChatDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Chat> getAllChats() {
        User currentUser = getCurrentUser();
        return chatRepository.findByUser(currentUser);
    }

    @Transactional
    public Chat createChat(ChatDto chatDto) {
        User currentUser = getCurrentUser();
        Chat chat = Chat.builder()
                .title(chatDto.title())
                .user(currentUser)
                .build();
        return chatRepository.save(chat);
    }

    public Chat getChatByExternalId(UUID externalId) {
        User currentUser = getCurrentUser();
        return chatRepository.findByExternalIdAndUser(externalId, currentUser)
                .orElseThrow(() -> new ChatNotFoundException(externalId));
    }

    @Transactional
    public Chat updateChat(UUID externalId, ChatDto chatDto) {
        User currentUser = getCurrentUser();
        Chat chat = chatRepository.findByExternalIdAndUser(externalId, currentUser)
                .orElseThrow(() -> new ChatNotFoundException(externalId));
        chat.setTitle(chatDto.title());
        return chatRepository.save(chat);
    }

    @Transactional
    public void deleteChat(UUID externalId) {
        User currentUser = getCurrentUser();
        Chat chat = chatRepository.findByExternalIdAndUser(externalId, currentUser)
                .orElseThrow(() -> new ChatNotFoundException(externalId));
        chatRepository.delete(chat);
    }

    public List<Message> getChatMessages(UUID chatExternalId) {
        User currentUser = getCurrentUser();
        return messageRepository.findByChatExternalIdOrderBySentAtDesc(chatExternalId, currentUser);
    }
}