package com.eltendawy.mymovies.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Base.BaseActivity;
import com.eltendawy.mymovies.Base.BaseFragment;
import com.eltendawy.mymovies.R;

import java.security.PublicKey;

/**
 * A simple {@link Fragment} subclass.
 */
public class Overview extends BaseFragment {

    public static final String TAG="overview";
    View view;
    TextView overView;
    private Movie movie;

    public Overview() {
        // Required empty public constructor
    }

    public Overview setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_overview, container, false);
        overView=view.findViewById(R.id.overview);
        if(movie!=null)
            overView.setText(movie.getOverview());
        // Inflate the layout for this fragment
        return view;
    }
    public void onStart() {
        super.onStart();
    }

    @NonNull
    @Override
    public String toString() {
        return TAG;
    }
}
