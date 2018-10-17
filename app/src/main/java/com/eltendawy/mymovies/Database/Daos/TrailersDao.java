package com.eltendawy.mymovies.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.eltendawy.mymovies.Api.Models.Trailer;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 9/21/2018.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
@Dao
public interface TrailersDao {

    @Query("select * from trailes where id = :id")
    public Trailer getTrailer(String id);
    @Insert(onConflict = REPLACE)
    public void AddTrailer(Trailer trailer);
    @Insert(onConflict = REPLACE)
    public void AddTrailers(List<Trailer> trailers);
    @Update
    public void UpdateTrailer(Trailer trailer);
    @Delete
    public void DeleteTrailer(Trailer trailer);
}
