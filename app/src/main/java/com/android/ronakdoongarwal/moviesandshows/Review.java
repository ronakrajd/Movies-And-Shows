package com.android.ronakdoongarwal.moviesandshows;

/**
 * Created by Development on 6/23/2016.
 */
public class Review {
    private String reviewAuthor;
    private String reviewContent;

    public Review(String reviewAuthor, String reviewContent) {
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }
}
