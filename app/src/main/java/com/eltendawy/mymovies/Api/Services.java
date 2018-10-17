package com.eltendawy.mymovies.Api;

import com.eltendawy.mymovies.Api.Models.MoviesResponse;
import com.eltendawy.mymovies.Api.Models.ReviewsResponse;
import com.eltendawy.mymovies.Api.Models.TrailersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {

    @GET("radio/radio_{language}.json")
    Call<Object> getModels(@Path("language") String language, @Query("key") String valu);

    @GET("3/discover/movie")
    Call<MoviesResponse>getMoviesList(@Query("api_key") String apiKey, @Query("sort_by") String sortBy,
                                      @Query("page") int page,@Query("include_adult") boolean includeAdult);
    @GET("3/movie/{movie_id}/reviews")
    Call<ReviewsResponse>getReviewsList(@Path("movie_id") int moview_id,@Query("api_key") String apiKey,
                                       @Query("page") int page);
    @GET("3/movie/{movie_id}/videos")
    Call<TrailersResponse>getTrailersList(@Path("movie_id") int moview_id, @Query("api_key") String apiKey);
}
