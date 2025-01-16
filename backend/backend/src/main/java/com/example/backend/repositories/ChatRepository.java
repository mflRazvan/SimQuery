package com.example.backend.repositories;

import com.example.backend.models.Chat;
import com.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUser(User user);
    Optional<Chat> findByExternalIdAndUser(UUID externalId, User user);
}