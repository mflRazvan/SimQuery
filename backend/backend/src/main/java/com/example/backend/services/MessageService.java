package com.example.backend.services;

import com.example.backend.exceptions.AIServiceException;
import com.example.backend.exceptions.MessageNotFoundException;
import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import com.example.backend.models.SenderType;
import com.example.backend.models.User;
import com.example.backend.repositories.MessageRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utils.SimilarityRequest;
import com.example.backend.utils.dtos.MessageResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final WebClient aiServiceWebClient;
    private final ObjectMapper objectMapper;

    public MessageService(
            MessageRepository messageRepository,
            ChatService chatService, UserRepository userRepository,
            @Qualifier("aiServiceWebClient") WebClient aiServiceWebClient,
            ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.userRepository = userRepository;
        this.aiServiceWebClient = aiServiceWebClient;
        this.objectMapper = objectMapper;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Message getMessageByExternalId(UUID externalId) {
        User currentUser = getCurrentUser();
        return messageRepository.findByExternalId(externalId)
                .filter(message -> message.getChat().getUser().equals(currentUser))
                .orElseThrow(() -> new MessageNotFoundException(externalId));
    }

    @Transactional
    public MessageResponseDto createMessage(UUID chatExternalId, String content, SenderType sender) {
        User currentUser = getCurrentUser();
        Chat chat = chatService.getChatByExternalId(chatExternalId);

        Message userMessage = Message.builder()
                .chat(chat)
                .content(content)
                .sender(sender)
                .build();

        Message savedUserMessage = messageRepository.save(userMessage);
        log.debug("User message saved: {}", savedUserMessage.getExternalId());

        MessageResponseDto.MessageDetails userMessageDetails = new MessageResponseDto.MessageDetails(
                savedUserMessage.getExternalId(),
                savedUserMessage.getContent(),
                savedUserMessage.getSender(),
                savedUserMessage.getSentAt()
        );

        MessageResponseDto.MessageDetails aiMessageDetails = null;
        if (sender == SenderType.USER) {
            try {
                String aiResponse = getAIResponse(savedUserMessage);
                Message aiMessage = Message.builder()
                        .chat(chat)
                        .content(aiResponse)
                        .sender(SenderType.CHATBOT_MODEL)
                        .build();
                Message savedAiMessage = messageRepository.save(aiMessage);

                aiMessageDetails = new MessageResponseDto.MessageDetails(
                        savedAiMessage.getExternalId(),
                        savedAiMessage.getContent(),
                        savedAiMessage.getSender(),
                        savedAiMessage.getSentAt()
                );
            } catch (Exception e) {
                log.error("Error getting AI response", e);
            }
        }

        return new MessageResponseDto(userMessageDetails, aiMessageDetails);
    }

    private String getAIResponse(Message userMessage) {
        SimilarityRequest request = new SimilarityRequest(userMessage.getContent());
        log.debug("Sending request to AI service: {}", request.getPrompt());

        String response = aiServiceWebClient.post()
                .uri("/get-similarity")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            String similarityScore = rootNode.get("similarity_score").asText();
            log.debug("Received similarity score: {}", similarityScore);
            return similarityScore;
        } catch (Exception e) {
            log.error("Failed to process AI response", e);
            throw new AIServiceException("Failed to process AI response", e);
        }
    }

    @Transactional
    public void deleteMessage(UUID externalId) {
        User currentUser = getCurrentUser();
        Message message = messageRepository.findByExternalId(externalId)
                .filter(msg -> msg.getChat().getUser().equals(currentUser))
                .orElseThrow(() -> new MessageNotFoundException(externalId));
        messageRepository.delete(message);
    }
}