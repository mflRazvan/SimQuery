package com.example.backend.exceptions;

import java.util.UUID;

public class MessageNotFoundException extends ResourceNotFoundException {
    public MessageNotFoundException(UUID externalId) {
        super("Message not found with external ID: " + externalId);
    }
}