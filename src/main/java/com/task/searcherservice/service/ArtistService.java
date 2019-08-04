package com.task.searcherservice.service;

import com.task.searcherservice.client.AppleClient;
import com.task.searcherservice.client.GoogleClient;
import com.task.searcherservice.dto.Album;
import com.task.searcherservice.dto.Book;
import com.task.searcherservice.exception.RemoteServiceException;
import java.time.Duration;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Alexander Shakhov
 */
@Service
@RequiredArgsConstructor
public class ArtistService {

    public static final int MAX_WAIT_INTERVAL = 60;
    @NonNull
    private final GoogleClient googleClient;
    @NonNull
    private final AppleClient appleClient;
    @NonNull
    private final AlbumService albumService;
    @NonNull
    private final BookService bookService;

    public Flux<List<?>> getTopBooksAndAlbums(@NonNull String input, @Nullable Integer limit) {
        return Flux.merge(monoAlbums(input, limit), monoBooks(input, limit))
                .subscribeOn(Schedulers.elastic());
    }

    private Mono<List<Album>> monoAlbums(@NonNull String input, @Nullable Integer limit) {
        return appleClient.getData(input)
                .map(str -> albumService.getTopAlbums(str, limit))
                .doOnError(error -> {
                    throw new RemoteServiceException(null, error.getMessage());
                })
                .onErrorResume(error -> Mono.empty())
                .take(Duration.ofSeconds(MAX_WAIT_INTERVAL))
                .log()
                .subscribeOn(Schedulers.elastic());
    }

    private Mono<List<Book>> monoBooks(@NonNull String input, @Nullable Integer limit) {
        return googleClient.getData(input)
                .map(str -> bookService.getTopBooks(str, limit))
                .doOnError(error -> {
                    throw new RemoteServiceException(null, error.getMessage());
                })
                .onErrorResume(error -> Mono.empty())
                .take(Duration.ofSeconds(MAX_WAIT_INTERVAL))
                .log()
                .subscribeOn(Schedulers.elastic());
    }
}
