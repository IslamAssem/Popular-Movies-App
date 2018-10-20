package com.eltendawy.mymovies.Database.Daos;

import android.arch.lifecycle.LiveData;
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
public interface TrailersDao extends BaseDao<Trailer>{

    @Query("select * from Trailer where id = :id")
    public LiveData<Trailer> getTrailerById(String id);
    @Query("select * from Trailer where movie_id = :id")
    public LiveData<List<Trailer>> getTrailersByMovieId(int id);
}
