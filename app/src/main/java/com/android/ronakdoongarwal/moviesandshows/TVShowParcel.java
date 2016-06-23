package com.android.ronakdoongarwal.moviesandshows;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Development on 6/4/2016.
 */
public class TVShowParcel implements Parcelable {
    String mTVShowTitle, mOverview, mReleaseDate, mPoster_url, mBackdropURL,convertedReleaseDate, mVoteCount;
    double mUserRating;
    int mshowId;
    public TVShowParcel(String showTitle, String overview, String releaseDate, String poster_url, String backdropURL, double userRating, String voteCount, int showId) {
        this.mTVShowTitle = showTitle;
        this.mPoster_url = poster_url;
        this.mOverview = overview;
        this.mBackdropURL = backdropURL;
        this.mUserRating = userRating;
        convertedReleaseDate = converReleaseDate(releaseDate);
        this.mReleaseDate = convertedReleaseDate;
        this.mVoteCount = voteCount;
        this.mshowId = showId;
    }

    private String converReleaseDate(String releaseDate) {
        String temp[] = releaseDate.split("-");
        String year = temp[0];
        String month = temp[1];
        String day = temp[2];
        return (day+"-"+month+"-"+year);
    }

    private TVShowParcel(Parcel in) {
        this.mTVShowTitle = in.readString();
        this.mPoster_url = in.readString();
        this.mOverview = in.readString();
        this.mUserRating = in.readDouble();
        this.mBackdropURL = in.readString();
        this.mReleaseDate = in.readString();
        this.mVoteCount = in.readString();
        this.mshowId = in.readInt();
    }
    public String getTVShowTitle() {
        return mTVShowTitle;
    }

    public String getVoteCount() {  return mVoteCount;}

    public String getImagePosterURL() {
        return mPoster_url;
    }

    public String getBackdropURL() {return mBackdropURL;}

    public String getOverview() {
        return mOverview;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public int getShowId(){return mshowId;}

    public String toString() {
        return mTVShowTitle + "--" +
                mPoster_url + "--" +
                mOverview + "--" +
                mUserRating + "--" +
                mReleaseDate+"--"+
                mBackdropURL+"--"+
                mVoteCount+"--"+
                mshowId; }

    public static final Creator<TVShowParcel> CREATOR = new Creator<TVShowParcel>() {
        @Override
        public TVShowParcel createFromParcel(Parcel in) {
            return new TVShowParcel(in);
        }

        @Override
        public TVShowParcel[] newArray(int size) {
            return new TVShowParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTVShowTitle);
        dest.writeString(mPoster_url);
        dest.writeString(mOverview);
        dest.writeDouble(mUserRating);
        dest.writeString(mBackdropURL);
        dest.writeString(mReleaseDate);
        dest.writeString(mVoteCount);
        dest.writeInt(mshowId);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final ImageView showPoster;
        public final TextView showTitle;
        public final TextView showRating;
        public final RatingBar showRatingBar;
        public final View popupButton;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            showPoster= (ImageView) itemView.findViewById(R.id.movie_poster);
            showTitle= (TextView) itemView.findViewById(R.id.movie_title);
            showRating= (TextView) itemView.findViewById(R.id.movie_rating);
            showRatingBar= (RatingBar) itemView.findViewById(R.id.ratingBar);
            popupButton=itemView.findViewById(R.id.popup_button);
        }
    }
}
