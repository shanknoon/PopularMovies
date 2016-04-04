package com.sankar.popularmovies;

/**
 * Created by muthu ganesan on 3/22/2016.
 */
public class Review {
    private String idReview="";
    private String author="";
    private String content="";
    private String reviewURL = null;
    private long idMovie;

    public String getIdReview() {
        return idReview;
    }

    public void setIdReview(String idReview) {
        this.idReview = idReview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewURL() {
        return reviewURL;
    }

    public void setReviewURL(String reviewURL) {
        this.reviewURL = reviewURL;
    }

    public long getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(long idMovie) {
        this.idMovie = idMovie;
    }

    @Override
    public String toString() {
        return "Review{" +
                "idReview=" + idReview +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", reviewURL=" + reviewURL +
                ", idMovie=" + idMovie +
                '}';
    }
}
