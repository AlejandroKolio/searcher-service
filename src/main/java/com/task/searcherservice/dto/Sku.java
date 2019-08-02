package com.task.searcherservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Sku implements Comparable<Sku> {
    private String wrapperType;
    private int artistId;
    private int collectionId;
    private int trackId;
    private String artistName;
    private String collectionName;
    private String trackName;
    private String collectionCensoredName;
    private String trackCensoredName;
    private String artistViewUrl;
    private String collectionViewUrl;
    private String trackViewUrl;
    private String previewUrl;
    private String artworkUrl30;
    private String artworkUrl60;
    private String artworkUrl100;
    private double collectionPrice;
    private double trackPrice;
    private String releaseDate;
    private String collectionExplicitness;
    private String trackExplicitness;
    private int discCount;
    private int discNumber;
    private int trackCount;
    private int trackNumber;
    private int trackTimeMillis;
    private String country;
    private String currency;
    private String primaryGenreName;
    private boolean isStreamable;

    @Override
    public int compareTo(Sku o) {
        final int last = collectionName.compareTo(o.collectionName);
        return last == 0 ? collectionName.compareTo(o.collectionName) : last;
    }
}
