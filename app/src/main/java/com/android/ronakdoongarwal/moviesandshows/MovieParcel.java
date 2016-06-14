package com.android.ronakdoongarwal.moviesandshows;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Development on 6/4/2016.
 */
public class MovieParcel  implements Parcelable {
    String mMovieTitle, mOverview, mReleaseDate, mPoster_url, mBackdrop_url;
    double mUserRating;

    public MovieParcel( String movieTitle, String overview, String releaseDate, String poster_url,String backdropURL, double userRating) {
        this.mMovieTitle = movieTitle;
        this.mPoster_url = poster_url;
        this.mOverview = overview;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
        this.mBackdrop_url = backdropURL;
    }

    private MovieParcel( Parcel in) {

        this.mMovieTitle = in.readString();
        this.mPoster_url = in.readString();
        this.mOverview = in.readString();
        this.mUserRating = in.readDouble();
        this.mReleaseDate = in.readString();
        this.mBackdrop_url = in.readString();
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

    public String getBackdropURL() {return mBackdrop_url;
    }
    public String toString() {
        return mMovieTitle + "--" +
                mPoster_url + "--" +
                mOverview + "--" +
                mUserRating + "--" +
                mReleaseDate+ "--"+ mBackdrop_url; }

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
        dest.writeString(mBackdrop_url);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final ImageView moviePoster;
        public final TextView movieTitle;
        public final TextView movieRating;
        public final RatingBar movieRatingBar;
        public final View popupButton;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            moviePoster= (ImageView) itemView.findViewById(R.id.movie_poster);
            movieTitle= (TextView) itemView.findViewById(R.id.movie_title);
            movieRating= (TextView) itemView.findViewById(R.id.movie_rating);
            movieRatingBar= (RatingBar) itemView.findViewById(R.id.ratingBar);
            popupButton=itemView.findViewById(R.id.popup_button);
        }
    }
}
