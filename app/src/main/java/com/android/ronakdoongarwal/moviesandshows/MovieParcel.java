package com.android.ronakdoongarwal.moviesandshows;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Development on 6/4/2016.
 */
public class MovieParcel implements Parcelable {
    String mMovieTitle, mOverview, mReleaseDate, mPoster_url;
    double mUserRating;
    public MovieParcel(String movieTitle, String overview, String releaseDate, String poster_url, double userRating) {
        this.mMovieTitle = movieTitle;
        this.mPoster_url = poster_url;
        this.mOverview = overview;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
    }

    private MovieParcel(Parcel in) {
        this.mMovieTitle = in.readString();
        this.mPoster_url = in.readString();
        this.mOverview = in.readString();
        this.mUserRating = in.readDouble();
        this.mReleaseDate = in.readString();
    }
    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getImagePosterURL() {
        return mPoster_url;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String toString() {
        return mMovieTitle + "--" +
                mPoster_url + "--" +
                mOverview + "--" +
                mUserRating + "--" +
                mReleaseDate; }

    public static final Creator<MovieParcel> CREATOR = new Creator<MovieParcel>() {
        @Override
        public MovieParcel createFromParcel(Parcel in) {
            return new MovieParcel(in);
        }

        @Override
        public MovieParcel[] newArray(int size) {
            return new MovieParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieTitle);
        dest.writeString(mPoster_url);
        dest.writeString(mOverview);
        dest.writeDouble(mUserRating);
        dest.writeString(mReleaseDate);
    }
}
