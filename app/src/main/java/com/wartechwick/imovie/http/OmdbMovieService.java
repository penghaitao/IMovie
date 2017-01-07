package com.wartechwick.imovie.http;

import com.wartechwick.imovie.model.Movies;
import com.wartechwick.imovie.model.Result;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by penghaitao on 2016/10/12.
 */
public interface OmdbMovieService {

    @GET("?i={imdbId}&plot=short&r=json")
    Observable<Movies<List<Result>>> getOmdbMovie();
}
