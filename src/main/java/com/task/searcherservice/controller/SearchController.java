package com.task.searcherservice.controller;

import com.task.searcherservice.service.ArtistService;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Alexander Shakhov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class SearchController {

    @NonNull
    private final ArtistService artistService;

    /**
     * Endpoint to search for both books and albums represented as an artist's entity
     * @return ResponseEntity<List < Artist>> a list of books.
     */
    @GetMapping(value = "/search/artist", consumes = MediaType.ALL_VALUE, produces =
            MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<List<?>> getTopBooksAndAlbums(@NonNull @RequestParam("name") final String artist,
            @Nullable @RequestParam(value = "limit", required = false) final Integer limit) {
        return artistService.getTopBooksAndAlbums(artist, Objects.isNull(limit) ? 5 : limit);
    }
}
