package com.example.backend.utils.dtos.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class SignupResponse {
    private String email;
    private String username;
    private String message;
}
