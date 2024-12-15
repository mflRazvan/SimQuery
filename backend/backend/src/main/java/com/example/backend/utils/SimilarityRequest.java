package com.example.backend.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimilarityRequest {
    private String prompt;

    public SimilarityRequest(String prompt) {
        this.prompt = prompt;
    }

}
