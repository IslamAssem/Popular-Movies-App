package com.eltendawy.mymovies.Database.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import com.eltendawy.mymovies.Api.Models.Movie;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 9/21/2018.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
@Dao
public interface MovieDao {

    @Query("select * from movies")
    public LiveData<List<Movie>> getAllMovies();
    @Query("select * from movies where id=:id")
    public LiveData<Movie> getMovie(int id);
    @Insert(onConflict = REPLACE)
    public void AddMovie(Movie movie);
    @Update
    public void UpdateMovie(Movie movie);
    @Delete
    public void DeleteMovie(Movie movie);
}
