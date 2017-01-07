package com.wartechwick.imovie.http;

import com.wartechwick.imovie.model.Movie;
import com.wartechwick.imovie.model.Movies;
import com.wartechwick.imovie.model.OmdbMovie;
import com.wartechwick.imovie.model.Result;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by penghaitao on 2016/9/16.
 */
public interface MovieService {
    @GET("now_playing")
    Observable<Movies<List<Result>>> getInTheatre(@Query("page") int page);

    @GET("popular")
    Observable<Movies<List<Result>>> getPopular(@Query("page") int page);

    @GET("upcoming")
    Observable<Movies<List<Result>>> getUpcoming(@Query("page") int page);

    @GET("{id}?append_to_response=trailers")
    Observable<Movie> getMovie(@Path("id") String movieId);

    @GET("?plot=short&r=json")
    Observable<OmdbMovie> getOmdbMovie(@Query("t") String name, @Query("y") String year);
}
