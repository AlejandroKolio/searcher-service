package com.task.searcherservice.service;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.searcherservice.dto.Book;
import com.task.searcherservice.dto.Item;
import com.task.searcherservice.dto.SearchResponse;
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
public class BookService {

    @Value("${google.query}")
    private String googleUri;
    @NonNull
    private final WebClient.Builder webClient;
    @NonNull
    private final ObjectMapper mapper;

    @NonNull
    public Optional<List<Book>> getTopBooks(@NonNull String input, @NonNull Integer limit) {
        try {
            final String body = webClient.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder.scheme("https")
                            .host(googleUri)
                            .path("/books/v1/volumes")
                            .queryParam("q", input)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return Optional.of(getBooks(limit, body));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ApiClientException("Exception occured while retrieveing books");
    }

    /**
     * Accept body as string and return back list of extracted books.
     * @param limit limit the list (ex: top 5 or top 10).
     * @param bodyResponse String representation of webclient response.
     * @return List<Book>
     */
    @NonNull
    private List<Book> getBooks(@NonNull Integer limit, @NonNull String bodyResponse) throws IOException {
        return mapper.readValue(bodyResponse, SearchResponse.class)
                .getItems()
                .stream()
                .map(Item::getVolumeInfo)
                .map(Item.VolumeInfo::getTitle)
                .filter(Objects::nonNull)
                .distinct()
                .limit(limit)
                .sorted(Comparator.naturalOrder())
                .map(Book::new)
                .collect(toList());
    }
}
