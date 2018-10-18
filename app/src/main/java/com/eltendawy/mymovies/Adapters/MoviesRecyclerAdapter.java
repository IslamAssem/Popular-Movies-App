package com.eltendawy.mymovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eltendawy.mymovies.Activities.MovieDetails;
import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Models.Movie;
import com.eltendawy.mymovies.R;
import com.eltendawy.mymovies.interfaces.OnItemCLickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Islam on 09-Oct-17.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.viewholder> {
    private Context context;
    private ArrayList<Movie> movies;
    private ImageView menuHeaderPoster;
    OnItemCLickListener onItemCLickListener;


    public MoviesRecyclerAdapter(Context context, ImageView menuHeaderPoster) {
        this.context = context;
        this.movies = new ArrayList<>(40);
        this.menuHeaderPoster = menuHeaderPoster;
    }

    public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies.clear();
        if(movies!=null)
            this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public Movie getMovie(int position) {
        if(position>movies.size()-1)
            return null;
        return movies.get(position);
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void addMovie(List<Movie> movies) {
        int startPosition = this.movies.size();
        this.movies.addAll(movies);
        notifyItemRangeChanged(startPosition, movies.size());
    }

    public void resetMovies() {
        this.movies.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewitem = LayoutInflater.from((viewGroup.getContext())).inflate(R.layout.recycler_item_movie, viewGroup, false);
        return new viewholder(viewitem);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {

        final Movie movie = movies.get(position);
        if (movie.getTitle() != null)
            holder.Title.setText(movie.getTitle());
        //holder.Category.setText(movie.get);
        holder.Rating.setText(String.valueOf(movie.getVoteAverage()));
        holder.Genre.setText(movie.getGenres());
        if (onItemCLickListener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemCLickListener.onClick(movie, v, holder.getAdapterPosition());
                }
            });
        // TODO if(movie.getGenre()!=null)
        //holder.Genre.setText(movie.getGenre());
//        int w=new Utility(context,Constants.IMAGE_SMALL_Size).getColumBestzWidth();
//        ConstraintLayout.LayoutParams parms = new ConstraintLayout.LayoutParams(w,w);
//        holder.poster.setLayoutParams(parms);

        if (movie.getPosterPath() != null)
            Picasso.with(context).
                    load(APIManager.IMAGES_URL.concat(context.getResources().getString(R.string.backdrop_size).concat(movie.getPosterPath()))).
                    placeholder(R.drawable.temp).
                    error(R.drawable.temp).
                    into(holder.poster);
        /* TODO
        if(position==0)
        {
            try{
                if(menuHeaderPoster !=null)
            Picasso.with(context).
                    load(Constants.IMAGE_BASE_URL.concat(Constants.IMAGE_Medium_SIZE).concat(movie.getBackdropPath())).
                    into(menuHeaderPoster);
        }catch(Exception x){}
        }*/

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView Title;
        TextView Rating;
        TextView Genre;

        viewholder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            Title = itemView.findViewById(R.id.movie_title);
            Rating = itemView.findViewById(R.id.movie_rating);
            Genre = itemView.findViewById(R.id.movie_genre);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetails.class);
                    intent.putExtra("movie", (Parcelable) movies.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

    }
}
