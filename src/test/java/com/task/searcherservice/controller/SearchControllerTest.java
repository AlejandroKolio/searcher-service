package com.task.searcherservice.controller;

import com.task.searcherservice.service.ArtistService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author Alexander Shakhov
 */
@SpringBootTest
public class SearchControllerTest {

    @Autowired
    private ArtistService artistService;

    @Test
    @Description("Verify we get only 2 lists. Books and Albums")
    public void getTopBooksAndAlbums() {
        final Flux<List<?>> flux = artistService.getTopBooksAndAlbums("beatles", null);

        StepVerifier.create(flux).expectSubscription().expectNextCount(2).verifyComplete();
    }
}
