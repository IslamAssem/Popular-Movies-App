package com.eltendawy.mymovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eltendawy.mymovies.Api.APIManager;
import com.eltendawy.mymovies.Api.Models.Trailer;
import com.eltendawy.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Islam on 10-Oct-17.
 */

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.viewholder> {

    private Context context;
    private ArrayList<Trailer> trailers;

    public TrailersRecyclerAdapter(Context context) {
        this.context = context;
        trailers = new ArrayList<>();
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void addTrailers(List<Trailer> trailers) {
        int positionStart=this.trailers.size();
        this.trailers.addAll(trailers);
        notifyItemRangeChanged(positionStart,trailers.size());
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewitem = LayoutInflater.from((parent.getContext())).inflate(R.layout.recycler_item_trailer,parent,false);
        return new viewholder(viewitem);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.trailer.setText(trailers.get(position).getName().toString());
        Picasso.with(context).load(
                 APIManager.TRAILERS_URL + trailers.get(position).getKey() + "/0.jpg"

        ).error(R.drawable.main_parallex_popular).
                into(holder.trview);
        //holder.rev.setText(trailers.get(positi
        // on).getmAuthor().concat(" :\n\n\n").concat(Reviews.get(position).getmContent()));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
    public  class viewholder extends RecyclerView.ViewHolder
    {
        public TextView trailer;
        public ImageView trview;
        View itemView;
        public viewholder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            trailer= (TextView) itemView.findViewById(R.id.trailer_name);
            trview=(ImageView)itemView.findViewById(R.id.trailer_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp=trailers.get(getAdapterPosition()).getKey();
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APIManager.TRAILERS_YOUTUBE_PERFIX.concat(
                            temp))));
                    ;

                }
            });
        }
    }
}
