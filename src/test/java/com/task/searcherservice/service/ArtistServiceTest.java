package com.task.searcherservice.service;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import reactor.core.publisher.Flux;

/**
 * @author Alexander Shakhov
 */
@SpringBootTest
public class ArtistServiceTest {

    @Autowired
    private ArtistService artistService;

    @Description("Test to check the limit of each list.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void getTopLimitedSizeOfBooksAndAlbums(int limit) {
        final Flux<List<?>> topBooksAndAlbums = artistService.getTopBooksAndAlbums("michael jackson", limit);
        final List<?> albums = topBooksAndAlbums.blockFirst();
        final List<?> books = topBooksAndAlbums.blockLast();
        Assertions.assertThat(albums).hasSize(limit);
        Assertions.assertThat(books).hasSize(limit);
    }

    @Test
    @Description("Test to check the defaul list size .")
    public void getTopDefaultSizeOfBooksAndAlbums() {
        final Flux<List<?>> topBooksAndAlbums = artistService.getTopBooksAndAlbums("michael jackson", null);
        final List<?> albums = topBooksAndAlbums.blockFirst();
        final List<?> books = topBooksAndAlbums.blockLast();
        Assertions.assertThat(albums).hasSize(5);
        Assertions.assertThat(books).hasSize(5);
    }
}
