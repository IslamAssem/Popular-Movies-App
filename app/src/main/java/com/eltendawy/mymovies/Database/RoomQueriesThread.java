package com.eltendawy.mymovies.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.eltendawy.mymovies.Api.Models.Movie;

import static com.eltendawy.mymovies.Base.Status.FINISHED;
import static com.eltendawy.mymovies.Base.Status.IDLE;
import static com.eltendawy.mymovies.Base.Status.LOADING;

public class RoomQueriesThread extends AsyncTask<Movie,Void,Movie> {

    private com.eltendawy.mymovies.Base.Status status;

    private MoviesDatabase db;
    private RoomQueriesThread() {
        status = IDLE;
    }

    public RoomQueriesThread CreateDB(Context context) {
        db=MoviesDatabase.getInstance();
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        status = LOADING;
    }

    @Override
    protected Movie doInBackground(Movie[] movies) {
        return null;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        status=FINISHED;
    }
}
