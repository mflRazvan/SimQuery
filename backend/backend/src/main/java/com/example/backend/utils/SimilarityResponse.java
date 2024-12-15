package com.example.backend.utils;

public class SimilarityResponse {
    public double similarityScore;

    public SimilarityResponse(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }
}
