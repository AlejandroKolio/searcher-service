package com.task.searcherservice.dto;

import com.devskiller.friendly_id.FriendlyId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Alexander Shakhov
 */
@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    private String id = FriendlyId.createFriendlyId();
    private final String name;
}
