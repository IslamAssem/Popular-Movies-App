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
import com.eltendawy.mymovies.Adapters.TrailersRecyclerAdapter;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.Review;
import com.eltendawy.mymovies.Api.Models.TrailersResponse;
import com.eltendawy.mymovies.Base.BaseFragment;
import com.eltendawy.mymovies.Base.Status;
import com.eltendawy.mymovies.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eltendawy.mymovies.Base.Status.FINISHED;
import static com.eltendawy.mymovies.Base.Status.IDLE;
import static com.eltendawy.mymovies.Base.Status.LOADING;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trailer extends BaseFragment {

    public static final String TAG="Trailer";
    View view;
    RecyclerView recycle;
    Movie movie;
    TrailersRecyclerAdapter adapter;
    LinearLayoutManager manager;
    MovieDetails parentActivity;
    Status status;
    public Trailer() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_review, container, false);
        intiView();
        return view;

    }

    public Trailer setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public Trailer setParentActivity(MovieDetails parentActivity) {
        this.parentActivity = parentActivity;
        return this;
    }

    private void intiView() {
        status=IDLE;
        recycle= view.findViewById(R.id.recycler);
        manager=new LinearLayoutManager(activity);
        adapter=new TrailersRecyclerAdapter(activity);
        recycle.setLayoutManager(manager);
        recycle.setAdapter(adapter);
        if(movie==null)
            return;
        fetchTrailers();
    }
    public void fetchTrailers() {
        if(status==LOADING||status==FINISHED)
            return;
        status=LOADING;
        parentActivity.showSnackbarLoading(R.string.connecting,parentActivity.getView());
        APIManager.getAPIS().getTrailersList(movie.getId(),APIManager.APIKEY).enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailersResponse> call, @NonNull Response<TrailersResponse> response) {
                status=IDLE;
                try {
                    parentActivity.hideSnackbar();
                    if (response.body() != null) {
                        adapter.addTrailers(response.body().getResults());
                        if(response.body().getResults().size()==adapter.getItemCount())
                        {
                            status=FINISHED;
                            parentActivity.setTrailerStatus(status);
                        }
                    }
                }catch (Exception ignored)
                {
                    parentActivity.showSnackbar(null,status);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailersResponse> call, @NonNull Throwable t) {
//todo handle failer  show refresh snackbar
                parentActivity.showSnackbar(null,status);
            }
        });
    }


    public ArrayList<com.eltendawy.mymovies.Api.Models.Trailer> getTrailers()
    {
        return adapter.getTrailers();
    }
    @NonNull
    @Override
    public String toString() {
        return TAG;
    }
}
