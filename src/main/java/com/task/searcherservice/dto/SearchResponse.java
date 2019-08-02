package com.task.searcherservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexander Shakhov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
    private Integer resultCount;
    private List<Sku> results;
    private String kind;
    private Integer totalItems;
    private List<Item> items;
}
