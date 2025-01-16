package com.example.backend.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimilarityRequest {
    private String prompt;

    public SimilarityRequest(String prompt) {
        this.prompt = prompt;
    }
}