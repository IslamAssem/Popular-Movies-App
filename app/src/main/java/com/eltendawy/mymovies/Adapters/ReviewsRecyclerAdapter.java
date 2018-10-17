package com.eltendawy.mymovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eltendawy.mymovies.Api.Models.Review;
import com.eltendawy.mymovies.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Islam on 10-Oct-17.
 */

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.viewholder> {

    private Context context;
    private ArrayList<Review> reviews;

    public ReviewsRecyclerAdapter(Context context) {
        this.context = context;
        reviews=new ArrayList<>();
    }

    public void addReviews(List<Review> reviews) {
        int positionStart=this.reviews.size();
        this.reviews.addAll(reviews);
        notifyItemRangeChanged(positionStart,reviews.size());
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewitem = LayoutInflater.from((parent.getContext())).inflate(R.layout.recycler_item_review,parent,false);
        return new viewholder(viewitem);
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {

        holder.review.setText(String.valueOf(position+1).concat(") ").
                concat(reviews.get(position).getAuthor().concat(" :\n").
                        concat(reviews.get(position).getContent())));
    }

    @Override
    public int getItemCount()
    {
        return reviews.size();
    }
    static class viewholder extends RecyclerView.ViewHolder
    {
        TextView review;
        viewholder(View itemView) {
            super(itemView);
            review=  itemView.findViewById(R.id.review);
        }
    }
}
