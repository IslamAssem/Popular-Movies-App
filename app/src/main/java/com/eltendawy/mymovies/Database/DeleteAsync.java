package com.eltendawy.mymovies.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.Review;
import com.eltendawy.mymovies.Api.Models.Trailer;
import com.eltendawy.mymovies.Database.MoviesDatabase;

import java.util.ArrayList;
import java.util.List;

public class DeleteAsync<T> extends AsyncTask<Void, Void, Void> {

    T t;

    public DeleteAsync(T t) {
        this.t = t;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            if (t instanceof Movie)
                MoviesDatabase.getInstance()
                        .movieDao().Delete((Movie) t);
            else if (t instanceof Review)
                MoviesDatabase.getInstance()
                        .reviewsDao().Delete((Review) t);
            else if (t instanceof Trailer)
                MoviesDatabase.getInstance()
                        .trailersDao().Delete((Trailer) t);
        } catch (Exception ignored) {
        }
        return null;
    }

}
