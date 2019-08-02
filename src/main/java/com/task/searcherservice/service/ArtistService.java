package com.task.searcherservice.service;

import com.task.searcherservice.dto.Artist;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Alexander Shakhov
 */
@Service
@RequiredArgsConstructor
@PropertySource("classpath:queries.properties")
public class ArtistService {

    @Value("${google.query}")
    private String booksUri;
    @Value("${itunes.query}")
    private String albumUri;
    @NonNull
    private final WebClient.Builder webClient;

    public List<Artist> getTopBooksAndAlbums(@NonNull String input) {
        final String body = webClient.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(booksUri).queryParam("q", input).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return null;
    }
}
