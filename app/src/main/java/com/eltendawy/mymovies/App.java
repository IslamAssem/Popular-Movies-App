package com.eltendawy.mymovies;

import android.app.Application;

import com.eltendawy.mymovies.Database.MoviesDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MoviesDatabase.setInstance(this);
    }
}
