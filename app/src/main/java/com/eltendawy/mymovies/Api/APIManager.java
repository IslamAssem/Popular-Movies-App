package com.eltendawy.mymovies.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 9/28/2018.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
public class APIManager {

    public static final String APIKEY= "accbfaa88d800f721a3f2229e18b5cb9";
    public static final String IMAGES_URL= "http://image.tmdb.org/t/p/";
    public static final String TRAILERS_URL = "http://img.youtube.com/vi/";
    public static final String TRAILERS_YOUTUBE_PERFIX = "http://www.youtube.com/watch?v=";

    private static Retrofit retrofitInstance;
    private static Retrofit getInstance(){
        if(retrofitInstance==null){
            //build
            String baseUrl = "http://api.themoviedb.org/";
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitInstance;
    }

    public static Services getAPIS(){
       return getInstance().create(Services.class);
    }

}
