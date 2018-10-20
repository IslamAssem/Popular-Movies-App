package com.eltendawy.mymovies.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.Review;
import com.eltendawy.mymovies.Api.Models.Trailer;
import com.eltendawy.mymovies.Database.Daos.MovieDao;
import com.eltendawy.mymovies.Database.Daos.ReviewsDao;
import com.eltendawy.mymovies.Database.Daos.TrailersDao;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 9/21/2018.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */

@Database(entities = {Movie.class,Review.class,Trailer.class},version = 1,exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase{

    private static MoviesDatabase myDataBase;

    public abstract MovieDao movieDao();
    public abstract ReviewsDao reviewsDao();
    public abstract TrailersDao trailersDao();

    public static void setInstance(Context context){
        myDataBase= Room.databaseBuilder(context.getApplicationContext(),
                MoviesDatabase.class, "myMoviesDB")
                .build();

    }

    public static MoviesDatabase getInstance(){

        return myDataBase;
    }





}
