package com.example.backend.utils.dtos.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String username;
    private String token;
}

