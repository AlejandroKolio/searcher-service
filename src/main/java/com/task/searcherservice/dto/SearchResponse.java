package com.task.searcherservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Base enityt for both books and albums requests.
 * @author Alexander Shakhov
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
    private Integer resultCount;
    private List<Sku> results;
    private String kind;
    private Integer totalItems;
    private List<Item> items;
}
