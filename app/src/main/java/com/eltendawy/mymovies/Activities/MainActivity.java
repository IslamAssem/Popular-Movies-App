package com.eltendawy.mymovies.Activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.eltendawy.mymovies.Adapters.EndlessRecyclerViewScrollListener;
import com.eltendawy.mymovies.Adapters.MoviesRecyclerAdapter;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Api.Models.MoviesResponse;
import com.eltendawy.mymovies.Api.configuration;
import com.eltendawy.mymovies.Base.BaseActivity;
import com.eltendawy.mymovies.Base.Status;
import com.eltendawy.mymovies.Database.MoviesDatabase;
import com.eltendawy.mymovies.R;
import com.eltendawy.mymovies.interfaces.OnItemCLickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eltendawy.mymovies.Base.Status.FINISHED;
import static com.eltendawy.mymovies.Base.Status.IDLE;
import static com.eltendawy.mymovies.Base.Status.LOADING;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Snackbar snackbar;
    int page;
    GridLayoutManager manager;
    private MoviesRecyclerAdapter mAdapter;
    private RecyclerView recycler;
    Status status;
    CoordinatorLayout parentLayout;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    MoviesDatabase db;

    /*, menueRecycler;
    FloatingActionButton fabsearch;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView menuim, DialogRefresh;
    private TextView DialogError_message;
    //private MenuAdapter menuAdapter;
    private Toolbar toolbar;
    private ArrayList<Movie> MovieList;
    //private ArrayList<com.eltendawy.popularmoviesapp.Models.MenuItem> menuList;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        initViews();
        fetchMovies();
    }

    private void initViews() {
        page = 1;
        db = MoviesDatabase.getInstance(context_application);
        setSort(configuration.discoverSort[1]);
        status = Status.IDLE;//used to stop page skipping as making many request increase page integer;
        parentLayout = findViewById(R.id.parent_layout);
        recycler = findViewById(R.id.recycler);
        mAdapter = new MoviesRecyclerAdapter(activity, null);
        manager = new GridLayoutManager(activity, getResources().getInteger(R.integer.span_count));
        recycler.setLayoutManager(manager);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!getSort().equals(configuration.Favourite))
                    fetchMovies();
            }
        };
        mAdapter.setOnItemCLickListener(new OnItemCLickListener() {
            @Override
            public void onClick(Object data, View view, int position) {
                Intent intent = new Intent(activity, MovieDetails.class);
                intent.putExtra(MOVIE_KEY, (Movie) data);
                startActivity(intent);
            }
        });
        recycler.setAdapter(mAdapter);
        recycler.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    private void fetchMovies() {
        showSnackbarLoading(R.string.retrying_connecting, recycler);
        if (getSort().equals(configuration.Favourite)) {
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
        APIManager.getAPIS().getMoviesList(APIManager.APIKEY, getSort(), page++, true).enqueue(new Callback<MoviesResponse>() {
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
                //TODO handle failure
                showSnackbar();
            }
        });
    }

    public void showSnackbar() {
        resetState(endlessRecyclerViewScrollListener, status);
        snackbar = Snackbar
                .make(recycler, R.string.error_connecting, Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchMovies();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.BLUE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_popular: {
                if (getSort().equals(configuration.discoverSort[1]))
                    break;
                mAdapter.resetMovies();
                page = 1;
                setSort(configuration.discoverSort[1]);
                fetchMovies();
                break;
            }
            case R.id.nav_top_rated: {
                if (getSort().equals(configuration.discoverSort[3]))
                    break;
                mAdapter.resetMovies();
                page = 1;
                setSort(configuration.discoverSort[3]);
                fetchMovies();
                break;
            }
            case R.id.nav_upcoming: {
                if (getSort().equals(configuration.discoverSort[5]))
                    break;
                mAdapter.resetMovies();
                page = 1;
                setSort(configuration.discoverSort[5]);
                fetchMovies();
                break;
            }
            case R.id.nav_favourite: {
                if (getSort().equals(configuration.Favourite))
                    break;
                mAdapter.resetMovies();
                page = 1;
                setSort(configuration.Favourite);
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
}
