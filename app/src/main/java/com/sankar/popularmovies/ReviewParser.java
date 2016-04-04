package com.sankar.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by muthu ganesan on 3/22/2016.
 */
public class ReviewParser {
    public ArrayList<Review> parseJsonString(String jsonStr){

        ArrayList<Review> reviewList = new ArrayList<>();
        String idReview;
        String author;
        String content;
        String review_path;
        long idMmovie;

        //MovieService movieService = MovieService.getInstance();
        //movieService.removeAllMovies();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            idMmovie = jsonObject.getLong("id");
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject reviewObject = (JSONObject)jsonArray.get(i);
                Review review = new Review();
                idReview = reviewObject.getString("id");
                review.setIdReview(idReview);
                author = reviewObject.getString("author");
                review.setAuthor(author);
                content = reviewObject.getString("content");
                review.setContent(content);
                review_path = reviewObject.getString("url");
                review.setReviewURL(review_path);
                review.setIdMovie(idMmovie);
                reviewList.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return reviewList;
    }
}
