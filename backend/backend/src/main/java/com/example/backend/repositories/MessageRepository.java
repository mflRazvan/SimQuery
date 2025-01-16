package com.example.backend.repositories;

import com.example.backend.models.Message;
import com.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByExternalId(UUID externalId);
    void deleteByExternalId(UUID externalId);

    // Modified method to find messages by chat and ensure user access
    @Query("SELECT m FROM Message m WHERE m.chat.externalId = :chatExternalId AND m.chat.user = :user ORDER BY m.sentAt DESC")
    List<Message> findByChatExternalIdOrderBySentAtDesc(
            @Param("chatExternalId") UUID chatExternalId,
            @Param("user") User user
    );
}