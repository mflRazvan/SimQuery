package com.example.backend.services;

import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import com.example.backend.repositories.MessageRepo;
import com.example.backend.utils.SimilarityRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private ChatService chatService;

    private WebClient webClient;

    @Autowired
    public MessageService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    public List<Message> getAll(){
        return messageRepo.findAll();
    }

    public Message getById(int id){
        return messageRepo.findById(id).get();
    }

    public Mono<String> add(Message message){
        if(message.getChatId() == -1){
            Chat chat = new Chat();
            chat.setTitle("New chat");
            chatService.add(chat);
            message.setChatId(chat.getId());
        }
        messageRepo.save(message);
        return getResponseFromModel(message);
    }

    public Mono<String> getResponseFromModel(Message sentMessage) {
        SimilarityRequest request = new SimilarityRequest(sentMessage.getContent());

        return webClient.post() // HTTP GET request
                .uri("/get-similarity/") // URI to make the call
                .bodyValue(request)
                .retrieve() // Initiates the request
                .bodyToMono(String.class) // Converts the response body to a Mono
                .flatMap(chatbotResponse -> {
                    try{
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(chatbotResponse);

                        String similarityScore = rootNode.get("similarity_score").asText();
                        Message chatResponse = new Message(0L, sentMessage.getChatId(), similarityScore, LocalDate.now(), LocalTime.now(), "model");
                        this.messageRepo.save(chatResponse);
                        return Mono.just(similarityScore);
                    } catch (Exception e){
                        System.out.println("Error parsing response: " + e.getMessage());
                        return Mono.error(new RuntimeException("Failed to parse the response from the model"));
                    }
                });
    }

    public void update(Message message){
        Message updatedMessage = messageRepo.findById(Math.toIntExact(message.getId())).orElse(null);
        if(updatedMessage != null) {
            updatedMessage.setId(message.getId());
            updatedMessage.setContent(message.getContent());
            updatedMessage.setSentDate(message.getSentDate());
            updatedMessage.setSentTime(message.getSentTime());
            messageRepo.save(updatedMessage);
        }
    }

    public void delete(int id){
        messageRepo.deleteById(id);
    }
}
