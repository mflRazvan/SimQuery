package com.example.backend.repositories;

import com.example.backend.models.Chat;
import com.example.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Integer> {
}
