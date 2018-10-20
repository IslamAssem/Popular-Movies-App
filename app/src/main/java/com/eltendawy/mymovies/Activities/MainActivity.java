package com.eltendawy.mymovies.Activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eltendawy.mymovies.Adapters.EndlessRecyclerViewScrollListener;
import com.eltendawy.mymovies.Adapters.MoviesRecyclerAdapter;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Configuration;
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.MoviesResponse;
import com.eltendawy.mymovies.Base.BaseActivity;
import com.eltendawy.mymovies.Base.Status;
import com.eltendawy.mymovies.Database.MoviesDatabase;
import com.eltendawy.mymovies.Fragments.SearchDialog;
import com.eltendawy.mymovies.R;
import com.eltendawy.mymovies.interfaces.OnItemCLickListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eltendawy.mymovies.Base.Status.ERROR;
import static com.eltendawy.mymovies.Base.Status.FINISHED;
import static com.eltendawy.mymovies.Base.Status.IDLE;
import static com.eltendawy.mymovies.Base.Status.LOADING;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnItemCLickListener, Callback<MoviesResponse> {
    private Snackbar snackbar;
    int page;
    GridLayoutManager manager;
    private MoviesRecyclerAdapter mAdapter;
    private RecyclerView recycler;
    Status status;
    CoordinatorLayout parentLayout;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    MoviesDatabase db;
    ImageView navBackdrop, navPoster;
    TextView navTitle;
    int randomPosition;
    SearchDialog dialog;
    boolean isSearching;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if(getSupportFragmentManager()!=null)
                        if(getSupportFragmentManager().findFragmentByTag(dialog.TAG)==null)
                            dialog.show(getSupportFragmentManager(),dialog.TAG);
                        else dialog.dismiss();
                }catch (Exception ignored){
                }

            }
        });
        intiNavigationLayout();
        initViews();
        fetchMovies();
    }

    private void intiNavigationLayout() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                randomPosition=-1;
                if (mAdapter.getItemCount() == 0) return;
                randomPosition = new Random().nextInt(mAdapter.getItemCount());
                Movie movie = mAdapter.getMovie(randomPosition);
                if (movie == null)
                    return;
                if (movie.getPosterPath() == null || movie.getPosterPath().equals("") || movie.getPosterPath().equals("null"))
                    movie.setPosterPath("");
                if (movie.getBackdropPath() == null || movie.getBackdropPath().equals("") || movie.getBackdropPath().equals("null"))
                    movie.setBackdropPath(movie.getPosterPath());
                navTitle.setText(movie.getTitle());
                Picasso.with(context_application).load(
                        APIManager.IMAGES_URL.concat(getResources().getString(R.string.poster_size)).concat(movie.getBackdropPath()
                        )).
                        into(navBackdrop);
                Picasso.with(context_application).load(
                        APIManager.IMAGES_URL.concat(getResources().getString(R.string.poster_size)).concat(movie.getPosterPath()
                        )).
                        into(navPoster);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(randomPosition!=-1)
                MainActivity.this.onClick(mAdapter.getMovie(randomPosition),v,randomPosition);
            }
        });
        navBackdrop = header.findViewById(R.id.nav_backdrop);
        navPoster = header.findViewById(R.id.nav_poster);
        navTitle = header.findViewById(R.id.nav_title);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initViews() {
        page = 1;
        randomPosition=-1;
        isSearching=false;
        dialog=new SearchDialog().setParent(this);
        db = MoviesDatabase.getInstance();
        setSort(Configuration.discoverSort[1]);
        status = Status.IDLE;//used to stop page skipping as making many request increase page integer;
        parentLayout = findViewById(R.id.parent_layout);
        recycler = findViewById(R.id.recycler);
        mAdapter = new MoviesRecyclerAdapter(activity, null);
        manager = new GridLayoutManager(activity, getResources().getInteger(R.integer.span_count));
        recycler.setLayoutManager(manager);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!getSort().equals(Configuration.Favourite))
                    fetchMovies();
            }
        };
        mAdapter.setOnItemCLickListener(this);
        recycler.setAdapter(mAdapter);
        recycler.addOnScrollListener(endlessRecyclerViewScrollListener);
        includeAdult=getSharedPrefrences(BaseActivity.sharedKeyAdult,false);
    }

    public void fetchMovies() {
        if(status==ERROR)
            showSnackbarLoading(R.string.retrying_connecting, recycler);
        else if(!getSort().equals(Configuration.Favourite)) showSnackbarLoading(R.string.connecting, recycler);
        else {
            db.movieDao().getAllMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    mAdapter.setMovies(movies);
                }
            });
            hideSnackbar();
            return;
        }
        if (status == FINISHED || status == LOADING)
            return;
        if(isSearching)
            APIManager.getAPIS().searchmovie(APIManager.APIKEY, getSort(), page++, includeAdult).enqueue(this);

        else
            APIManager.getAPIS().getMoviesList(APIManager.APIKEY, getSort(), page++, includeAdult).enqueue(this);

    }


    public void fetchMovies(String keyword) {
        page=1;
        status=LOADING;
        isSearching=true;
        setSort(keyword);
        resetState(endlessRecyclerViewScrollListener);
        showSnackbarLoading(R.string.connecting,recycler);
        mAdapter.setMovies(null);
        APIManager.getAPIS().searchmovie(APIManager.APIKEY, getSort(), page++, includeAdult).enqueue(this);
    }

    public void showSnackbar() {

        status = ERROR;
        resetState(endlessRecyclerViewScrollListener);
        snackbar = Snackbar
                .make(recycler, R.string.error_connecting, Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchMovies();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.error));
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_include_adult) {
            includeAdult=!includeAdult;
            SaveSharedPrefrences(BaseActivity.sharedKeyAdult,includeAdult);
            item.setChecked(includeAdult);
            mAdapter.resetMovies();
            page=1;
            status=IDLE;
            fetchMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_include_adult).setChecked(includeAdult);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        isSearching=false;
        switch (id) {
            case R.id.nav_popular: {
                if (getSort().equals(Configuration.discoverSort[1]))
                    break;
                mAdapter.resetMovies();
                page = 1;
                setSort(Configuration.discoverSort[1]);
                fetchMovies();
                break;
            }
            case R.id.nav_top_rated: {
                if (getSort().equals(Configuration.discoverSort[3]))
                    break;
                mAdapter.resetMovies();
                page = 1;
                setSort(Configuration.discoverSort[3]);
                fetchMovies();
                break;
            }
            case R.id.nav_upcoming: {
                if (getSort().equals(Configuration.discoverSort[5]))
                    break;
                mAdapter.resetMovies();
                page = 1;
                setSort(Configuration.discoverSort[5]);
                fetchMovies();
                break;
            }
            case R.id.nav_favourite: {
                if (getSort().equals(Configuration.Favourite))
                    break;
                status=IDLE;
                mAdapter.resetMovies();
                page = 1;
                setSort(Configuration.Favourite);
                fetchMovies();
                break;
            }
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(Object data, View view, int position) {
        Intent intent = new Intent(activity, MovieDetails.class);
        intent.putExtra(MOVIE_KEY, (Movie) data);
        startActivity(intent);

    }

    @Override
    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
        status = IDLE;
        hideSnackbar();
        try {
            if (response.body() != null) {
                mAdapter.addMovie(response.body().getResults());
                if (response.body().getTotalPages() < page)
                    status = FINISHED;
            }
        } catch (Exception ignored) {
            showSnackbar();
        }
    }

    @Override
    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {

        showSnackbar();
    }
}
