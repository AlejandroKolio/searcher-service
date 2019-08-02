package com.task.searcherservice.service;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.searcherservice.dto.Album;
import com.task.searcherservice.dto.SearchResponse;
import com.task.searcherservice.dto.Sku;
import com.task.searcherservice.exception.ApiClientException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
public class AlbumService {

    @Value("${itunes.query}")
    private String itunesUri;
    @NonNull
    private final WebClient.Builder webClient;
    @NonNull
    private final ObjectMapper mapper;

    @NonNull
    public Optional<List<Album>> getTopAlbums(@NonNull String input, @NonNull Integer limit) {
        try {
            final String body = webClient.build().get().uri(uriBuilder ->
                uriBuilder
                        .scheme("https")
                        .host(itunesUri)
                        .path("/search")
                        .queryParam("term", input)
                        .build())
                    .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();
            return Optional.of(getAlbums(limit, body));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ApiClientException("Exception occured while retrieveing albums");
    }

    /**
     * Accept bodyResponse as string and return back list of extracted albums.
     * @param limit limit the list (ex: top 5 or top 10).
     * @param bodyResponse String representation of webclient response.
     * @return List<Album>
     */
    @NonNull
    private List<Album> getAlbums(@NonNull Integer limit, @NonNull String bodyResponse) throws IOException {
        return mapper.readValue(bodyResponse, SearchResponse.class)
                        .getResults()
                        .stream()
                        .map(Sku::getCollectionName)
                        .filter(Objects::nonNull)
                        .distinct()
                        .limit(limit)
                        .sorted(Comparator.naturalOrder())
                        .map(Album::new)
                        .collect(toList());
    }
}
