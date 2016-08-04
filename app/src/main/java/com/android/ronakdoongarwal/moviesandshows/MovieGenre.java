package com.android.ronakdoongarwal.moviesandshows;

/**
 * Created by Development on 8/3/2016.
 */
public class MovieGenre {
    int genreId;
    String genre;

    public MovieGenre(int genreId, String genre) {
        this.genreId = genreId;
        this.genre = genre;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getGenre() {
        return genre;
    }
}
