package com.example.backend.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SimilarityResponse(
        @JsonProperty("similarity_score")
        double similarityScore
) {}