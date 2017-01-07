package com.wartechwick.imovie.http;

import com.wartechwick.imovie.app.Constants;
import com.wartechwick.imovie.model.Movie;
import com.wartechwick.imovie.model.Movies;
import com.wartechwick.imovie.model.OmdbMovie;
import com.wartechwick.imovie.model.Result;
import com.wartechwick.imovie.tools.AppUtils;
import com.wartechwick.imovie.tools.UIUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by penghaitao on 2016/9/19.
 */
public class HttpMethods {

    String BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit tmdbRetrofit;
    private MovieService movieService;
    private Retrofit omdbRetrofit;
    private MovieService omdbMovieService;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", Constants.API_KEY).build();
                if (!AppUtils.isNetworkReachable(UIUtils.getContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .url(url).build();
                } else {
                    request = request.newBuilder()
                            .url(url).build();
                }

                Response response = chain.proceed(request);
                if (AppUtils.isNetworkReachable(UIUtils.getContext())) {
                    int maxAge = 60 * 60; // read from cache for 1 minute
                    response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                return response;
            }
        };

        File httpCacheDirectory = new File(UIUtils.getContext().getExternalCacheDir(), "responses");
        OkHttpClient client = new OkHttpClient.Builder().cache(new Cache(httpCacheDirectory,10 * 1024 * 1024)).addInterceptor(interceptor).build();

        tmdbRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        movieService = tmdbRetrofit.create(MovieService.class);
        Retrofit omdbRetrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        omdbMovieService = omdbRetrofit.create(MovieService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void getMovieList(Subscriber<List<Result>> subscriber, String title, int page) {
        Observable observable;
        switch (title) {
            case "INTHEATRE":
                observable = movieService.getInTheatre(page)
                        .map(new MovieFunc<List<Result>>());
                break;
            case "POPULAR":
                observable = movieService.getPopular(page)
                        .map(new MovieFunc<List<Result>>());
                break;
            default:
                observable = movieService.getUpcoming(page)
                        .map(new MovieFunc<List<Result>>());
                break;
        }
        toSubscribe(observable, subscriber);
    }

    public void getMovie(Subscriber<Movie> subscriber, String id, String title, String year) {
        Observable observable = Observable.zip(movieService.getMovie(id), omdbMovieService.getOmdbMovie(title, year), new Func2<Movie, OmdbMovie, Movie>() {
            @Override
            public Movie call(Movie movie, OmdbMovie omdbMovie) {
                movie.setImdbRating(omdbMovie.getImdbRating());
                movie.setMetaScore(omdbMovie.getMetaScore());
                return movie;
            }
        });
        toSubscribe(observable, subscriber);
    }

    private void toSubscribe(Observable o, Subscriber s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    private class MovieFunc<T> implements Func1<Movies<T>, T> {

        @Override
        public T call(Movies<T> httpResult) {
            List<Result> movies = new ArrayList<Result>();
            for (int i=0; i<((List<Result>) httpResult.getResults()).size(); i++) {
                Result result = ((List<Result>) httpResult.getResults()).get(i);
                if (result.getPosterPath() != null) {
                    movies.add(result);
                }
            }
            return (T) movies;
        }
    }

}
