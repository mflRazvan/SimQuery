package com.example.backend.services;

import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import com.example.backend.repositories.ChatRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatService {
    @Autowired
    ChatRepo chatRepo;

    public List<Chat> getAll(){
        return chatRepo.findAll();
    }

    public Chat getById(int id){
        return chatRepo.findById(id).get();
    }

    public void add(Chat chat){
        chatRepo.save(chat);
    }

    public void update(Chat chat){
        Chat updatedChat = chatRepo.findById(Math.toIntExact(chat.getId())).orElse(null);
        if(updatedChat != null) {
            updatedChat.setId(chat.getId());
            updatedChat.setMessages(chat.getMessages());
            chatRepo.save(updatedChat);
        }
    }

    public void delete(int id){
        chatRepo.deleteById(id);
    }

    public List<Message> getChatsMessages(int chatId){
        return this.chatRepo.getMessagesByChat(chatId);
    }
}
