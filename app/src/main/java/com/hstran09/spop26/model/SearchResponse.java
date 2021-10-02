package com.hstran09.spop26.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchResponse {
    private Integer count;
    @SerializedName("next_offset")
    private Integer nextOffset;
    private Integer total;
    private Double took;
    private List<Result> results;

    @Data
    public static class Result {
        private String audio;
        @SerializedName("description_original")
        private String descriptionOriginal;
        @SerializedName("title_original")
        private String titleOriginal;
        private String image;
        private String thumbnail;
        private String id;
        @SerializedName("listennotes_url")
        private String listennotesUrl;
        private String link;
        private Podcast podcast;

        @Data
        public static class Podcast {
            @SerializedName("listennotes_url")
            private String listennotesUrl;
            private String id;
            @SerializedName("title_original")
            private String titleOriginal;
            @SerializedName("publisher_original")
            private String publisherOriginal;
            private String image;
            private String thumbnail;
            @SerializedName("genre_ids")
            private List<Integer> genreIds;
        }
    }
}
