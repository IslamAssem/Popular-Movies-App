package com.eltendawy.mymovies.Database;

import android.os.AsyncTask;
import android.util.Log;

import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.Review;
import com.eltendawy.mymovies.Api.Models.Trailer;

import java.util.ArrayList;
import java.util.List;

public class InsertAsync<T> extends AsyncTask<Void, Void, Void> {

    private List<T> Ts;
    private int MovieId;
    public InsertAsync(List<T> Ts,int movie_id) {
        this.Ts = Ts;
        MovieId=movie_id;
    }

    public InsertAsync(T t) {
        Ts = new ArrayList<>();
        Ts.add(t);
        MovieId=-1;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            T t = Ts.get(0);
            if (t instanceof Movie) {
                Log.e("insert", t.toString());
                MoviesDatabase.getInstance()
                        .movieDao().insert((ArrayList<Movie>) Ts);
            } else if (t instanceof Review) {
                for(Review review:(ArrayList<Review>)Ts)
                    review.setMovie_id(MovieId);

                MoviesDatabase.getInstance()
                        .reviewsDao().insert((ArrayList<Review>) Ts);
            } else if (t instanceof Trailer) {
                Log.e("insert", Ts.size() + " " + t.toString());
                for(Trailer trailer:(ArrayList<Trailer>)Ts)
                    trailer.setMovie_id(MovieId);
                MoviesDatabase.getInstance()
                        .trailersDao().insert((ArrayList<Trailer>) Ts);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
