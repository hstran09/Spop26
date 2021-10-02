package com.hstran09.spop26.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Episode implements Serializable {

    private String id;
    private String link;
    private String audio;
    private String image;
    private String title;
    private String thumbnail;
    private String transcript;
    private String description;
}

