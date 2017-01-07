package com.wartechwick.imovie.model;

import com.google.gson.annotations.SerializedName;
import com.wartechwick.imovie.app.Constants;

import java.util.List;

/**
 * Created by penghaitao on 2016/10/4.
 */
public class Movie {

    @SerializedName("tagline")
    private String tagline;
    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("runtime")
    private String runtime;
    @SerializedName("imdb_id")
    private String imdbId;
    @SerializedName("vote_average")
    private String rating;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("original_language")
    private String language;
    @SerializedName("trailers")
    private Trailer trailer;
    @SerializedName("homepage")
    private String homePage;
    @SerializedName("genres")
    private List<Genre> genreList;

    private String imdbRating;

    private String metaScore;

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

    public String getGenre() {
        String genre = "";
        int size = genreList.size();
        if (size > 3) {
            size = 3;
        }
        if (genreList != null && genreList.size()>0) {
            for (int i=0; i<size; i++) {
                genre+=genreList.get(i).getName();
                if (i != size-1) {
                    genre+=", ";
                }
            }
        }
        return genre;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return Constants.IMAGE_W780_BASEURL + backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public class Trailer {
        @SerializedName("youtube")
        private List<Youtube> youtubeList;

        public List<Youtube> getYoutubeList() {
            return youtubeList;
        }

        public void setYoutubeList(List<Youtube> youtubeList) {
            this.youtubeList = youtubeList;
        }

        private String getTrailerId() {
            String trailerId = "";
            if (youtubeList != null && youtubeList.size() > 0) {
                for (int i=0; i<youtubeList.size(); i++) {
                    String type = youtubeList.get(i).getType();
                    if ("Trailer".equals(type)) {
                        trailerId = youtubeList.get(i).getSource();
                        break;
                    } else {
                        trailerId = youtubeList.get(0).getSource();
                    }
                }
            }
            return trailerId;
        }

        public String getTrailerUrl() {
            return Constants.YOUTUBE_TRAILER_BASEURL+getTrailerId();
        }

        public String getTrailerImageUrl() {
            return Constants.TRAILER_IMG_PREFIX + getTrailerId() + Constants.TRAILER_IMG_SUFFIX;
        }
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public class Youtube {
        @SerializedName("name")
        private String typeNmae;
        @SerializedName("type")
        private String type;
        @SerializedName("source")
        private String source;

        public String getTypeNmae() {
            return typeNmae;
        }

        public void setTypeNmae(String typeNmae) {
            this.typeNmae = typeNmae;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    public class Genre {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
