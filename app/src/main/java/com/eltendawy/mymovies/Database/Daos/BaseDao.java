package com.eltendawy.mymovies.Database.Daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.eltendawy.mymovies.Api.Models.Movie;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

public interface  BaseDao<T>{

    @Insert(onConflict = REPLACE)
    void insert(T model);
    @Insert(onConflict = REPLACE)
    void insert(List<T> models);
    //@Update
    //public void Update(T model);
    @Delete
    void Delete(T model);
    @Update
    void Update(T model);



}
