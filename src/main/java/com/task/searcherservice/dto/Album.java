package com.task.searcherservice.dto;

import com.devskiller.friendly_id.FriendlyId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Alexander Shakhov
 */
@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    private String albumId = FriendlyId.createFriendlyId();
    private final String albumTitle;
}
