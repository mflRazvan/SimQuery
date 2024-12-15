package com.example.backend.repositories;

import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Integer> {
    @Query("SELECT m FROM Message m WHERE m.chatId = :chatId")
    List<Message> getMessagesByChat(@Param("chatId") int chatId);
}
