package com.eltendawy.mymovies.Fragments;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eltendawy.mymovies.Activities.MovieDetails;
import com.eltendawy.mymovies.Adapters.EndlessRecyclerViewScrollListener;
import com.eltendawy.mymovies.Adapters.ReviewsRecyclerAdapter;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Configuration;
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.ReviewsResponse;
import com.eltendawy.mymovies.Base.BaseFragment;
import com.eltendawy.mymovies.Base.Status;
import com.eltendawy.mymovies.Database.MoviesDatabase;
import com.eltendawy.mymovies.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eltendawy.mymovies.Base.Status.ERROR;
import static com.eltendawy.mymovies.Base.Status.FINISHED;
import static com.eltendawy.mymovies.Base.Status.IDLE;
import static com.eltendawy.mymovies.Base.Status.LOADING;

/**
 * A simple {@link Fragment} subclass.
 */
public class Review extends BaseFragment {

    public static final String TAG = "review";
    View view;
    RecyclerView recycle;
    Movie movie;
    ReviewsRecyclerAdapter adapter;
    LinearLayoutManager manager;
    int page;
    MovieDetails parentActivity;
    Status status;
    public int totalReviews;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    boolean tryOnline;
    public Review() {
        page = 1;
        status=IDLE;
        tryOnline=false;
        totalReviews=0;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review, container, false);
        intiView();
        return view;

    }

    public Review setMovie(Movie movie) {
        this.movie = movie;
        page=movie.getLastReviewsPage();
        return this;
    }

    public Review setParentActivity(MovieDetails parentActivity) {
        this.parentActivity = parentActivity;
        return this;
    }

    private void intiView() {
        recycle = view.findViewById(R.id.recycler);
        manager = new LinearLayoutManager(activity);
        adapter = new ReviewsRecyclerAdapter(activity);
        recycle.setLayoutManager(manager);
        recycle.setAdapter(adapter);
        endlessRecyclerViewScrollListener=new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchReviews();
            }
        };
        if (movie == null)
            return;
        recycle.addOnScrollListener(endlessRecyclerViewScrollListener);
        fetchReviews();
    }

    public void fetchReviews() {
        switch (status)
        {
            case FINISHED: {
                parentActivity.setReviewStatus(status);
                return;
            }
            case LOADING: {
                return;
            }
            case ERROR:{
                parentActivity.showSnackbarLoading(R.string.retrying_connecting,parentActivity.getView());
                break;
            }
            case IDLE:{
                if(parentActivity.getSort().equals(Configuration.Favourite))
                {
                    if(tryOnline)
                        break;
                    MoviesDatabase.getInstance(parentActivity.getApplicationContext()).reviewsDao().
                            getReviewsByMovieId(movie.getId()).observe(this, new Observer<List<com.eltendawy.mymovies.Api.Models.Review>>() {
                        @Override
                        public void onChanged(@Nullable List<com.eltendawy.mymovies.Api.Models.Review> reviews) {
                            tryOnline=true;
                            adapter.addReviews(reviews);
                            parentActivity.hideSnackbar();
                            if(adapter.getItemCount()<movie.getNumerReviews())
                            {
                                page=movie.getLastReviewsPage();
                                status=IDLE;
                                fetchReviews();
                            }
                            else status=FINISHED;
                        }
                    });
                    parentActivity.showSnackbarLoading(R.string.reading_database,parentActivity.getView());
                    status=LOADING;
                    return;
                }
                parentActivity.showSnackbarLoading(R.string.connecting,parentActivity.getView());
                break;
            }
        }
        status=LOADING;

        APIManager.getAPIS().getReviewsList(movie.getId(), APIManager.APIKEY, page++).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                status=IDLE;
                try {
                    parentActivity.hideSnackbar();
                    if(response.body() != null) {
                        adapter.addReviews(response.body().getResults());
                        totalReviews=response.body().getTotalResults();
                        if(response.body().getTotalPages()<page)
                        {
                            status=FINISHED;
                            parentActivity.setReviewStatus(status);
                        }
                    }
                } catch (Exception e) {
                    status=ERROR;
                    parentActivity.showSnackbar(endlessRecyclerViewScrollListener);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
                status=ERROR;
                parentActivity.showSnackbar(endlessRecyclerViewScrollListener);
            }
        });
    }


    public ArrayList<com.eltendawy.mymovies.Api.Models.Review> getReviews()
    {
        return adapter.getReviews();
    }
    @NonNull
    @Override
    public String toString() {
        return TAG;
    }
}
