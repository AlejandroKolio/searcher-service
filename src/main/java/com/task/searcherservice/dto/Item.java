package com.task.searcherservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Alexander Shakhov
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String kind;
    private String id;
    private String etag;
    private String selfLink;
    private VolumeInfo volumeInfo;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VolumeInfo implements Comparable<VolumeInfo> {
        private String title;
        private List<String> authors;
        private String publishedDate;
        private String printType;
        private String maturityRating;
        private Boolean allowAnonLogging;
        private String contentVersion;
        private String language;
        private String previewLink;
        private String infoLink;
        private String canonicalVolumeLink;

        @Override
        public int compareTo(VolumeInfo o) {
            final int last = title.compareTo(o.title);
            return last == 0 ? title.compareTo(o.title) : last;
        }
    }
}
