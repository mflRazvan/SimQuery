package com.example.backend.exceptions;

import java.util.UUID;

public class ChatNotFoundException extends ResourceNotFoundException {
    public ChatNotFoundException(UUID externalId) {
        super("Chat not found with external ID: " + externalId);
    }
}