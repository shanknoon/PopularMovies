package com.sankar.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by muthu ganesan on 3/22/2016.
 */
public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.MyViewHolder> {

    private List<Trailer> trailerList;
    ClickListener clickListener;
    Context mContext;

    public VideoRecyclerAdapter(Context context, List<Trailer> trailerList, MovieDetailsFragment fragment) {
        this.trailerList = trailerList;
        clickListener = (ClickListener) fragment;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView icon;
        public TextView videoName;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            icon = (ImageView) view.findViewById(R.id.icon);
            videoName = (TextView) view.findViewById(R.id.trailerName);
        }

        @Override
        public void onClick(View v) {
            clickListener.itemClicked(v , getAdapterPosition());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailerlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        //holder.icon.setImageResource(R.drawable.youtube_play);
        //holder.videoName.setText(trailer.getName()+" "+(++position));
        holder.videoName.setText(trailer.getName());

        Picasso.with(mContext).load("http://img.youtube.com/vi/"+trailer.getKey()+"/default.jpg").placeholder(R.drawable.youtube_play).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}

