package com.eltendawy.mymovies.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.ReviewsResponse;
import com.eltendawy.mymovies.Base.BaseFragment;
import com.eltendawy.mymovies.Base.Status;
import com.eltendawy.mymovies.R;

import java.util.ArrayList;

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
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    public Review() {
        page = 1;
        status=IDLE;
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
        if(status==FINISHED||status==LOADING)
            return;
        status=LOADING;
        parentActivity.showSnackbarLoading(R.string.connecting,parentActivity.getView());
        APIManager.getAPIS().getReviewsList(movie.getId(), APIManager.APIKEY, page++).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                status=IDLE;
                try {
                    parentActivity.hideSnackbar();
                    if(response.body() != null) {
                        adapter.addReviews(response.body().getResults());
                        if(response.body().getTotalPages()<page)
                        {
                            status=FINISHED;
                            parentActivity.setReviewStatus(status);
                        }
                    }
                } catch (Exception ignored) {
                    parentActivity.showSnackbar(endlessRecyclerViewScrollListener,status);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
//todo handle failer  show refresh snackbar
                parentActivity.showSnackbar(endlessRecyclerViewScrollListener,status);
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
