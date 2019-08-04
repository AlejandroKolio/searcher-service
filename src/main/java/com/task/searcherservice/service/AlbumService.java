package com.task.searcherservice.service;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.searcherservice.dto.Album;
import com.task.searcherservice.dto.SearchResponse;
import com.task.searcherservice.dto.Sku;
import com.task.searcherservice.exception.ApiClientException;
import com.task.searcherservice.exception.FieldNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Shakhov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {

    @NonNull
    private final ObjectMapper mapper;

    /**
     * Accept body as string and return back list of extracted albums.
     * @param limit limit the list (ex: top 5 or top 10).
     * @param body String representation of webclient response.
     * @return List<Album>
     */
    @NonNull
    public List<Album> getTopAlbums(@NonNull String body, @Nullable Integer limit) {
        try {
            return Optional.ofNullable(mapper.readValue(body, SearchResponse.class).getResults())
                    .orElseThrow(() -> new FieldNotFoundException("field format is changed or not found"))
                    .stream()
                    .map(Sku::getCollectionName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .limit(Objects.isNull(limit) ? 5 : limit)
                    .sorted(Comparator.naturalOrder())
                    .map(Album::new)
                    .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IO exception while processing albums: ", e.getCause());
        }
        throw new ApiClientException("Error while processing albums from response");
    }
}
