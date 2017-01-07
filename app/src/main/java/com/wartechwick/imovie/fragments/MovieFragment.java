package com.wartechwick.imovie.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wartechwick.imovie.R;
import com.wartechwick.imovie.activities.MovieDetailActivity;
import com.wartechwick.imovie.adapters.MovieListAdapter;
import com.wartechwick.imovie.http.HttpMethods;
import com.wartechwick.imovie.model.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import tr.xip.errorview.ErrorView;

/**
 * Created by penghaitao on 2016/9/16.
 */
public class MovieFragment extends Fragment implements MovieListAdapter.IClickItem {

    private MovieListAdapter listAdapter;
    private Subscriber subscriber;
    private String tabTitle;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.error_view)
    ErrorView mErrorView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    int currentPage = 1;
    private Handler handler;
    private Runnable runnable;

    public MovieFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);
        mErrorView.setConfig(ErrorView.Config.create()
                .title(getString(R.string.error_title_damn))
                .titleColor(ContextCompat.getColor(this.getActivity(), R.color.dark))
                .subtitle(getString(R.string.error_details))
                .retryText(getString(R.string.error_retry))
                .build());
        mErrorView.setVisibility(View.GONE);
        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                getMovies(tabTitle);
                mErrorView.setVisibility(View.GONE);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        return view;
    }

    private void refresh() {
        getMovies(tabTitle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabTitle = getArguments().getString("title");
        getMovies(tabTitle);
    }

    private void getMovies(final String title) {
        subscriber = new Subscriber<List<Result>>() {
            @Override
            public void onCompleted() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                }
                mErrorView.setVisibility(View.VISIBLE);
//                int code = ((HttpException) e).code();
//                mErrorView.setError(code);
//                Toast.makeText(getActivity(), "code="+code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Result> results) {
                setMovieListData(results);
            }
        };
        callMovieList(title, 1);
    }

    private void loadMoreMovies(String title) {
        subscriber = new Subscriber<List<Result>>() {
            @Override
            public void onCompleted() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                }
                mErrorView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(List<Result> results) {
                listAdapter.updateResult(results);
            }
        };
        callMovieList(title, currentPage++);
    }

    private void callMovieList(final String title, final int page) {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);

        runnable = new Runnable() {
            @Override
            public void run() {
                HttpMethods.getInstance().getMovieList(subscriber, title, page);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    private void setMovieListData(List<Result> results) {
        listAdapter = new MovieListAdapter(getActivity(), tabTitle, results);
        listAdapter.setIClickItem(this);
        mRecyclerView.setAdapter(listAdapter);
        final StaggeredGridLayoutManager gridLayoutManager;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            gridLayoutManager = new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new StaggeredGridLayoutManager(5,
                    StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    boolean isBottom = ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPositions(new int[2])[1]
                            >= mRecyclerView.getLayoutManager().getItemCount() - 2;
                    if (!swipeRefreshLayout.isRefreshing() && isBottom) {
//                        Toast.makeText(getActivity(), "itemcount="+mRecyclerView.getLayoutManager().getItemCount()+"---"+((StaggeredGridLayoutManager)mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPositions(new int[2])[1], Toast.LENGTH_SHORT).show();
                        loadMoreMovies();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    private void loadMoreMovies() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setEnabled(false);
        currentPage++;
        loadMoreMovies(tabTitle);
    }

    @Override
    public void onClickItem(Result item, View view) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MovieDetailActivity.class);
        intent.putExtra("title", item.getTitle());
        intent.putExtra("id", item.getId() + "");
        intent.putExtra("rate", item.getVoteAverage());
        intent.putExtra("releaseDate", item.getReleaseDate());
        intent.putExtra("overview", item.getOverview());
        intent.putExtra("tab", tabTitle);
        startActivity(intent);
    }
}
