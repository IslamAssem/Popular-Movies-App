package com.eltendawy.mymovies.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Base.BaseActivity;
import com.eltendawy.mymovies.Base.BaseFragment;
import com.eltendawy.mymovies.Fragments.Overview;

import java.util.ArrayList;
//import com.android.internal.
/**
 * Created by Islam on 10-Oct-17.
 */

public class SimpleFragmentPagerAdapter  extends FragmentPagerAdapter {
    private ArrayList<Fragment>fragments;
    private ArrayList<String>titles;
    Movie movie;
    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public SimpleFragmentPagerAdapter setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public  void add(Fragment fragment, String title)
    {
        if(fragments==null||titles==null)
        {
            fragments=new ArrayList<>();
            titles=new ArrayList<>();
        }
        fragments.add(fragment);
        titles.add(title);

    }
    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment=(BaseFragment) fragments.get(position);
        return fragment;

//        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return fragments.size();
    }

    // This determines the title for each tab
    @Override
    public String getPageTitle(int position) {
        return  titles.get(position);
        }
    }



