package com.eltendawy.mymovies.Database.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.eltendawy.mymovies.Api.Models.Review;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 9/21/2018.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
@Dao
public interface ReviewsDao {

    @Query("select * from reviews where id= :id")
    public List<Review> getReview(String id);
    @Insert(onConflict = REPLACE)
    public void AddReview(Review review);

    @Insert(onConflict = REPLACE)
    public void AddReviews( List<Review> reviews);
    @Update
    public void UpdateReview(Review review);
    @Delete
    public void DeleteReview(Review review);
}
