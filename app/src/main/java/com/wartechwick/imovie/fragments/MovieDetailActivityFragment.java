package com.wartechwick.imovie.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wartechwick.imovie.R;
import com.wartechwick.imovie.activities.PhotoActivity;
import com.wartechwick.imovie.http.HttpMethods;
import com.wartechwick.imovie.model.Movie;
import com.wartechwick.imovie.tools.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import tr.xip.errorview.ErrorView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.detail_title)
    TextView titleView;
    @BindView(R.id.detail_tagline)
    TextView taglineView;
    @BindView(R.id.detail_overview)
    TextView overviewView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdrop)
    ImageView backdropView;
    @BindView(R.id.detail_imdb_rating)
    TextView imdbRatingView;
    @BindView(R.id.detail_meta_score)
    TextView metaScoreView;
    @BindView(R.id.tv_imdb_rating)
    TextView imdbRatingTextView;
    @BindView(R.id.tv_meta_score)
    TextView metaScoreTextView;
    @BindView(R.id.tvRating)
    TextView tvRating;
    @BindView(R.id.detail_rating)
    TextView ratingView;
    @BindView(R.id.detail_released)
    TextView releasedView;
    @BindView(R.id.detail_certification)
    TextView certificationView;
    @BindView(R.id.detail_runtime)
    TextView runtimeView;
    @BindView(R.id.detail_language)
    TextView languageView;
    @BindView(R.id.detail_youtube)
    ImageView trailerImageView;
    @BindView(R.id.play_button)
    ImageView playView;
    @BindView(R.id.trailorView)
    FrameLayout trailorView;
    @BindView(R.id.header)
    LinearLayout header;
    @BindView(R.id.trailorBackground)
    LinearLayout trailorBackground;
    @BindView(R.id.score_layout)
    LinearLayout scoreLayout;
    @BindView(R.id.imdb_layout)
    LinearLayout imdbLayout;
    @BindView(R.id.meta_layout)
    LinearLayout metaLayout;
    @BindView(R.id.error_view)
    ErrorView errorView;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;


    private String trailerUrl = null;
    private String backdrop = null;
    private String imdbId = null;
    private String homePage = "";

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trailorView.setOnClickListener(this);
        backdropView.setOnClickListener(this);
        scoreLayout.setOnClickListener(this);
        header.setOnClickListener(this);
        initContent();
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                initContent();
                scrollView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void initContent() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String id = intent.getStringExtra("id");
            String releaseDate = intent.getStringExtra("releaseDate");
            getMovie(id, title, StringUtils.getYear(releaseDate));
            titleView.setText(title);
        }
    }

    private void getMovie(String id, String title, String year) {

        Subscriber subscriber = new Subscriber<Movie>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                scrollView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                errorView.setError(((HttpException) e).code());
                Log.i("pp",e.getMessage());
            }

            @Override
            public void onNext(Movie movie) {
                taglineView.setText(movie.getTagline());
                overviewView.setText(movie.getOverview());
                if ("N/A".equals(movie.getImdbRating()) && "N/A".equals(movie.getMetaScore())) {
                    ratingView.setText(movie.getRating());
                    ratingView.setVisibility(View.VISIBLE);
                    tvRating.setVisibility(View.VISIBLE);
                } else {
                    imdbLayout.setVisibility(View.VISIBLE);
                    metaLayout.setVisibility(View.VISIBLE);
                    imdbRatingView.setText(movie.getImdbRating());
                    metaScoreView.setText(movie.getMetaScore());
                }
                releasedView.setText(movie.getReleaseDate());
                runtimeView.setText(movie.getRuntime());
                languageView.setText(movie.getLanguage());
                certificationView.setText(movie.getGenre());
                trailerUrl = movie.getTrailer().getTrailerUrl();
                imdbId = movie.getImdbId();
                homePage = movie.getHomePage();
                String trailerThumbnail = movie.getTrailer().getTrailerImageUrl();
                Glide.with(getActivity()).load(trailerThumbnail).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        trailerImageView.setImageBitmap(resource);
                        playView.setVisibility(View.VISIBLE);
                    }

                });
                backdrop = movie.getBackdropPath();
                Glide.with(getActivity()).load(backdrop).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        backdropView.setImageBitmap(resource);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette p) {
                                // Use generated instance
                                Palette.Swatch swatch = p.getVibrantSwatch();
                                Palette.Swatch trailorSwatch = p.getDarkVibrantSwatch();

                                if (swatch != null) {
                                    header.setBackgroundColor(swatch.getRgb());
                                    titleView.setTextColor(swatch.getTitleTextColor());
                                    taglineView.setTextColor(swatch.getBodyTextColor());
                                    overviewView.setTextColor(swatch.getBodyTextColor());
                                }
                                if (trailorSwatch != null) {
                                    trailorBackground.setBackgroundColor(trailorSwatch.getRgb());
                                    tvRating.setTextColor(trailorSwatch.getTitleTextColor());
                                    ratingView.setTextColor(trailorSwatch.getBodyTextColor());
                                    imdbRatingTextView.setTextColor(trailorSwatch.getTitleTextColor());
                                    imdbRatingView.setTextColor(trailorSwatch.getBodyTextColor());
                                    metaScoreTextView.setTextColor(trailorSwatch.getTitleTextColor());
                                    metaScoreView.setTextColor(trailorSwatch.getBodyTextColor());
                                }
                            }
                        });
                    }
                });
            }


        };
        HttpMethods.getInstance().getMovie(subscriber, id, title, year);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trailorView:
                if (trailerUrl != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl)));
                }
                break;
            case R.id.backdrop:
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                intent.putExtra("backdrop", backdrop);
                startActivity(intent);
                break;
            case R.id.score_layout:
                if (imdbId != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/" + imdbId)));
                }
                break;
            case R.id.header:
                if (!"".equals(homePage) && (homePage.startsWith("http://") || homePage.startsWith("https://"))) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(homePage)));
                }
        }
    }
}
