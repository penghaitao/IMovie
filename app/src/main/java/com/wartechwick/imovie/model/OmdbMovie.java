package com.wartechwick.imovie.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by penghaitao on 2016/10/13.
 */
public class OmdbMovie {

    @SerializedName("imdbRating")
    private String imdbRating;
    @SerializedName("Metascore")
    private String metaScore;
    @SerializedName("Title")
    private String title;

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getMetaScore() {
        return metaScore;
    }

    public void setMetaScore(String metaScore) {
        this.metaScore = metaScore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
