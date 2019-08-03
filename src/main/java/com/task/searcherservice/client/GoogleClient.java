package com.task.searcherservice.client;

import java.nio.charset.StandardCharsets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Alexander Shakhov
 */
@Service
@RequiredArgsConstructor
@PropertySource("classpath:queries.properties")
public class GoogleClient implements Client {
    @Value("${google.query}")
    private String googleUri;
    @NonNull
    private final WebClient webClient;

    @Override
    public Mono<String> getData(@NonNull String input) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host(googleUri)
                        .path("/books/v1/volumes")
                        .queryParam("q", input)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class);
    }
}
