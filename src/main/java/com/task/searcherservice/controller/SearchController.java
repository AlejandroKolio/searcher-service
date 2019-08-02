package com.task.searcherservice.controller;

import com.task.searcherservice.dto.Album;
import com.task.searcherservice.dto.Book;
import com.task.searcherservice.exception.ApiClientException;
import com.task.searcherservice.service.AlbumService;
import com.task.searcherservice.service.BookService;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Shakhov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class SearchController {
    @NonNull
    private final AlbumService albumService;
    @NonNull
    private final BookService bookService;

    /**
     * Endpoint to search for artist's albums
     * @return ResponseEntity<List<Album>> a list of albums.
     */
    @GetMapping("/search/albums")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Album>> getTopAlbums(@NonNull @RequestParam("artist") final String artist,
            @Nullable @RequestParam(value = "limit", required = false) final Integer limit) {
        final List<Album> albums = albumService.getTopAlbums(artist, Objects.isNull(limit) ? 5 : limit)
                .orElseThrow(ApiClientException::new);

        return ResponseEntity.ok(albums);
    }

    /**
     * Endpoint to search for artist's books
     * @return ResponseEntity<List<Book>> a list of books.
     */
    @GetMapping("/search/books")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Book>> getTopBooks(@NonNull @RequestParam("artist") final String artist,
            @Nullable @RequestParam(value = "limit", required = false) final Integer limit) {
        final List<Book> books = bookService.getTopBooks(artist, Objects.isNull(limit) ? 5 : limit)
                .orElseThrow(ApiClientException::new);

        return ResponseEntity.ok(books);
    }
}
