package com.example.backend.controllers;

import com.example.backend.exceptions.AIServiceException;
import com.example.backend.exceptions.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AIServiceException.class)
    public ResponseEntity<ErrorResponse> handleAIServiceException(AIServiceException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("AI service error: " + ex.getMessage()),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse("An unexpected error occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Authentication failed: " + ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Access denied: " + ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Invalid credentials"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(WebClientResponseException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(WebClientResponseException.Unauthorized ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Unauthorized request: " + ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(WebClientResponseException.Forbidden.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(WebClientResponseException.Forbidden ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Forbidden request: " + ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Collect all validation error messages
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = errors.isEmpty()
                ? "Validation failed"
                : String.join("; ", errors);

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(errorMessage));
    }

    public record ErrorResponse(String message) {}
}