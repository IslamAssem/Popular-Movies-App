package com.eltendawy.mymovies.Fragments;


import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
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
import com.eltendawy.mymovies.Adapters.TrailersRecyclerAdapter;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Configuration;
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.TrailersResponse;
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
public class Trailer extends BaseFragment {

    public static final String TAG="Trailer";
    View view;
    RecyclerView recycle;
    Movie movie;
    TrailersRecyclerAdapter adapter;
    LinearLayoutManager manager;
    MovieDetails parentActivity;
    Status status;
    boolean tryOnline;
    public Trailer() {
        tryOnline=false;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_review, container, false);
        intiView();
        return view;

    }

    public Status getStatus() {
        return status;
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
        switch (status)
        {
            case FINISHED: {
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
                    MoviesDatabase.getInstance().trailersDao().
                            getTrailersByMovieId(movie.getId()).observe(this, new Observer<List<com.eltendawy.mymovies.Api.Models.Trailer>>() {
                        @Override
                        public void onChanged(@Nullable List<com.eltendawy.mymovies.Api.Models.Trailer> trailers) {
                            tryOnline=true;
                            adapter.addTrailers(trailers);
                            parentActivity.hideSnackbar();
                            status=FINISHED;
                            fetchTrailers();
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
        APIManager.getAPIS().getTrailersList(movie.getId(),APIManager.APIKEY).enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailersResponse> call, @NonNull Response<TrailersResponse> response) {
                try {
                    parentActivity.hideSnackbar();
                    if (response.body() != null) {
                        adapter.addTrailers(response.body().getResults());
                        if(response.body().getResults().size()==adapter.getItemCount())
                        {
                            status=FINISHED;
                        }
                    }
                }catch (Exception e)
                {
                    status=ERROR;
                    parentActivity.showSnackbar(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailersResponse> call, @NonNull Throwable t) {
                status=ERROR;
                parentActivity.showSnackbar(null);
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
