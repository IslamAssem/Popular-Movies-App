package com.eltendawy.mymovies.Base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.eltendawy.mymovies.Adapters.EndlessRecyclerViewScrollListener;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.eltendawy.mymovies.Base.Status.LOADING;

public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity activity;
    protected Context context_application;
    private int main_fragment_container;
    protected Fragment quran, ahadith, radio;
    public static final String MOVIE_KEY = "movie";
    private static String sort;
    public static boolean includeAdult;
    Snackbar snackbar;
    public final static String sharedKeyAdult="adult";

    Toast toast;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        BaseActivity.sort = sort;
    }

    public BaseActivity() {
        activity = this;
        main_fragment_container = -1;
        includeAdult=false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_application = getApplicationContext();


    }

    public void setMain_fragment_container(int main_fragment_container) {
        this.main_fragment_container = main_fragment_container;
    }

    protected void showFragment(Fragment fragment) throws Exception {
        if (main_fragment_container == -1) throw new Exception("set main fragment container first");
        getSupportFragmentManager().beginTransaction().replace(main_fragment_container, fragment).commit();

    }

    public void showSnackbarLoading(int messageResourceId, View view) {
        snackbar = Snackbar
                .make(view, messageResourceId, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE);
        ViewGroup contentLay = (ViewGroup) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        ProgressBar item = new ProgressBar(activity);
        contentLay.addView(item);
        snackbar.show();
    }
    public void showToast(String message,int length) {
        try {
            if (toast != null)
                toast.cancel();
            toast=Toast.makeText(activity,message,length);
            toast.show();
        }catch (Exception ignored)
        {
        }
    }

    public void hideSnackbar()
    {
        try{
            if(snackbar!=null)
                snackbar.dismiss();
        }
        catch (Exception ignored)
        {
        }
    }
    public void resetState(EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener) {

        try{
            hideSnackbar();
            endlessRecyclerViewScrollListener.resetState();
        }catch (Exception ignored){
        }
    }
    public static final String sharedPreferencesName= "MyMoviesSetting";
    public void  SaveSharedPrefrences(String key,String value) {

        //save to value with the key in SharedPreferences
        SharedPreferences.Editor editor
                =getSharedPreferences(sharedPreferencesName,MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }
    public String getSharedPrefrences(String key,String defValue){
        SharedPreferences sharedPreferences =
                getSharedPreferences(sharedPreferencesName,MODE_PRIVATE);
        return  sharedPreferences.getString(key,defValue);
    }
    public void  SaveSharedPrefrences(String key,boolean value) {

        //save to value with the key in SharedPreferences
        SharedPreferences.Editor editor
                =getSharedPreferences(sharedPreferencesName,MODE_PRIVATE).edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean getSharedPrefrences(String key,boolean defValue){
        SharedPreferences sharedPreferences =
                getSharedPreferences(sharedPreferencesName,MODE_PRIVATE);
        return  sharedPreferences.getBoolean(key,defValue);
    }
}
