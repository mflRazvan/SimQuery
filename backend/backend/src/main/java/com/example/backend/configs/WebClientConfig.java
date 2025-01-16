package com.example.backend.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Bean
    public WebClient aiServiceWebClient() {
        return WebClient.builder()
                .baseUrl(aiServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .filter((request, next) -> {
                    // Log the request for debugging
                    System.out.println("Making request to: " + request.url());
                    return next.exchange(request)
                            .onErrorResume(e -> {
                                System.err.println("Error in request: " + e.getMessage());
                                return Mono.error(e);
                            });
                })
                .build();
    }
}