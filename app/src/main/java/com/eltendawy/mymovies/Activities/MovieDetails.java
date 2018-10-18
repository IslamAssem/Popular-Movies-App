package com.eltendawy.mymovies.Activities;

import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eltendawy.mymovies.Adapters.EndlessRecyclerViewScrollListener;
import com.eltendawy.mymovies.Adapters.SimpleFragmentPagerAdapter;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.Base.BaseActivity;
import com.eltendawy.mymovies.Base.Status;
import com.eltendawy.mymovies.Database.MoviesDatabase;
import com.eltendawy.mymovies.Fragments.Overview;
import com.eltendawy.mymovies.Fragments.Review;
import com.eltendawy.mymovies.Fragments.Trailer;
import com.eltendawy.mymovies.R;
import com.squareup.picasso.Picasso;

public class MovieDetails extends BaseActivity {
    MoviesDatabase db;
    View parent;
    Movie movie;
    ImageView moviewImage;
    TextView title, rating, release, genre;
    SimpleFragmentPagerAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    Overview overviewFragment;
    Review reviewFragment;
    Trailer trailersFragment;
    Snackbar snackbar;
    static Status status;
    FloatingActionButton fabFavourite;
    Status reviewStatus, trailerStatus;
    boolean isDbBusy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        db=MoviesDatabase.getInstance(context_application);
        initViews();

    }
    public void setReviewStatus(Status reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public void setTrailerStatus(Status trailerStatus) {
        this.trailerStatus = trailerStatus;
    }

    public void setMovie(Movie movie) {
        if(movie.isFavourite())
        this.movie.setFavourite(movie.isFavourite());
    }

    private void initViews() {
        status = Status.IDLE;
        isDbBusy=false;
        reviewStatus=Status.IDLE;
        trailerStatus=Status.IDLE;
        moviewImage = findViewById(R.id.movie_img);
        title = findViewById(R.id.movie_title);
        rating = findViewById(R.id.movie_rating);
        release = findViewById(R.id.movie_release);
        genre = findViewById(R.id.movie_genre);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        parent = findViewById(R.id.parent_layout);
        fabFavourite=findViewById(R.id.fab_fav);
        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie.isFavourite())
                    try
                    {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                isDbBusy=true;
                                db.movieDao().Delete(movie);
                            }
                        });
                        fabFavourite.setImageResource(R.drawable.ic_star_normal_24dp);
                        movie.setFavourite(false);
                    }catch (Exception ignored)
                    {
                    }
                else if(reviewStatus!=Status.LOADING&&trailerStatus!=Status.LOADING)
                try
                {
                    movie.setNumerReviews(reviewFragment.totalReviews);
                    movie.setLastReviewsPage(reviewFragment.getPage());
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            db.movieDao().insert(movie);
                            movie.setFavourite(true);
                        }
                    });
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<reviewFragment.getReviews().size();i++)
                                reviewFragment.getReviews().get(i).setMovie_id(movie.getId());
                            db.reviewsDao().insert(reviewFragment.getReviews());
                        }
                    });
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<trailersFragment.getTrailers().size();i++)
                                trailersFragment.getTrailers().get(i).setMovie_id(movie.getId());
                            db.trailersDao().insert(trailersFragment.getTrailers());
                        }
                    });
                    fabFavourite.setImageResource(R.drawable.ic_star_favorite_24dp);
                    movie.setFavourite(true);
                }catch (Exception ignored)
                {
                }
                else if(reviewStatus==Status.FINISHED&&trailerStatus!=Status.FINISHED)
                {
                    showToast(getResources().getString(R.string.trailers_list_not_loaded),
                            Toast.LENGTH_SHORT);
                }
                else if(reviewStatus!=Status.FINISHED&&trailerStatus==Status.FINISHED)
                {
                    showToast(getResources().getString(R.string.reviews_list_not_loaded),
                        Toast.LENGTH_SHORT);
                }
                else showToast(getResources().getString(R.string.trailers_reviews_loading_error),
                            Toast.LENGTH_SHORT);
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        }
        //////////////////////////////////////////////////
        movie = getIntent().getParcelableExtra(MOVIE_KEY);
        if (movie == null) return;
        //////////////////////////////////////////////////
        db.movieDao().getMovie(movie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if(movie==null)return;
                if(movie.isFavourite()){
                    setMovie(movie);
                    if(movie.isFavourite())
                        fabFavourite.setImageResource(R.drawable.ic_star_favorite_24dp);
                    else fabFavourite.setImageResource(R.drawable.ic_star_normal_24dp);
                }
            }
        });
        adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager()).setMovie(movie);
        overviewFragment = new Overview().setMovie(movie);
        reviewFragment = new Review().setParentActivity(this).setMovie(movie);
        trailersFragment = new Trailer().setParentActivity(this).setMovie(movie);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(movie.getTitle());

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        adapter.add(overviewFragment, overviewFragment.toString());
        adapter.add(reviewFragment, reviewFragment.toString());
        adapter.add(trailersFragment, trailersFragment.toString());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        title.setText(movie.getTitle());
        genre.setText(movie.getGenres());
        rating.setText(String.valueOf(movie.getVoteAverage()).concat("/10"));
        release.setText(movie.getReleaseDate().concat("|").concat(movie.getOriginalLanguage()));
        if (movie.getPosterPath() == null || movie.getPosterPath().equals("") || movie.getPosterPath().equals("null"))
            movie.setPosterPath("");
        if (movie.getBackdropPath() == null || movie.getBackdropPath().equals("") || movie.getBackdropPath().equals("null"))
            movie.setBackdropPath(movie.getPosterPath());
        Picasso.with(context_application).load(
                APIManager.IMAGES_URL.concat(getResources().getString(R.string.poster_size)).concat(movie.getBackdropPath()
                )).
                error(R.drawable.main_parallex_popular).
                placeholder(R.drawable.main_parallex_popular).
                into(moviewImage);
        //todo simpleviewpager
    }

    public void showSnackbar(EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener) {
        //if(Constants.current_type.equals(Constants.Fav))return;
        //if current movies are fav no need to reload
        resetState(endlessRecyclerViewScrollListener);
        status = Status.ERROR;
        snackbar = Snackbar
                .make(parent, R.string.error_connecting, Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSnackbarLoading(R.string.retrying_connecting, parent);
                        reviewFragment.fetchReviews();
                        trailersFragment.fetchTrailers();
                        status = Status.LOADING;
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

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public View getView() {
        return parent;
    }
}
