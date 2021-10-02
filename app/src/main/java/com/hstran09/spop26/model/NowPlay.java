package com.hstran09.spop26.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NowPlay {
    private String id;
    private String link;
    private String audio;
    private String image;
    private String title;
    private String thumbnail;
    private String description;

    private Podcast podcast;

    @Data
    @Accessors(chain = true)
    public static class Podcast {
        private String id;
        private String title;
        private String publisher;
        @SerializedName("total_episodes")
        private Integer totalEpisodes;
        private String description;
        private String website;
    }
}
