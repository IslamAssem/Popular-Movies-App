package com.eltendawy.mymovies.Base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
    private String sort;
    Snackbar snackbar;

    Toast toast;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public BaseActivity() {
        activity = this;
        main_fragment_container = -1;
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
    public void resetState(EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener, Status status) {

        try{
            status = Status.ERROR;
            hideSnackbar();
            endlessRecyclerViewScrollListener.resetState();
        }catch (Exception ignored){
        }
    }

}
