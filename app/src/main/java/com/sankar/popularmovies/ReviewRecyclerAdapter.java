package com.sankar.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

/**
 * Created by muthu ganesan on 3/22/2016.
 */
public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.MyViewHolder> {

    private List<Review> reviewList;

    public ReviewRecyclerAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView author, content;

            public MyViewHolder(View view) {
                super(view);

                author = (TextView) view.findViewById(R.id.review_headlines);
                content = (TextView) view.findViewById(R.id.review_details);
            }

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reviewlist, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Review review = reviewList.get(position);
            holder.author.setText("A movie reviw by "+review.getAuthor());
            holder.content.setText(review.getContent());
        }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }
}
