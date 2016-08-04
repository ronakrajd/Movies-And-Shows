package com.android.ronakdoongarwal.moviesandshows;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by Development on 6/5/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieParcel.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private MoviesFragment mActivityFragment;
    private String mBaseImgStr = "";
    public List<MovieParcel> movieParcelList;

    public MovieAdapter(Context context, MoviesFragment activityFragment, List<MovieParcel> movieParcelList) {
        //super(context, R.layout.movie_grid_layout, imageUrls);
        Log.e("crash", "MovieAdapter: " );
        this.mContext = context;
        this.mActivityFragment = activityFragment;
        this.mBaseImgStr = mActivityFragment.getString(R.string.api_base_image_url);
        this.movieParcelList = movieParcelList;
    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        MovieParcel movieParcel = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_layout, parent, false);
//        }
//        ImageView tempImageView = (ImageView) (convertView.findViewById(R.id.movie_poster));
//        String posterURL = movieParcel.getImagePosterURL();
//        Log.e("crash error", "getView: ");
//        String movieTitle = movieParcel.getMovieTitle();
//        TextView movie_tv = (TextView) convertView.findViewById(R.id.movie_title);
//        movie_tv.setText(movieTitle);
//
//        TextView user_rating = (TextView) convertView.findViewById(R.id.movie_rating);
//        double rating = movieParcel.getUserRating();
//        user_rating.setText(String.valueOf(rating));
//        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
//        ratingBar.setStepSize(0.001f);
//        ratingBar.setRating((float) rating);
//
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
//        int screenWidth = displaymetrics.widthPixels;
//
//        // eventual width of each image
//        int newImageWidth = (screenWidth / mActivityFragment.getNumGridViewCols());
//
//        // original dimensions : mImageWidth and mImageHeight
//        int newImageHeight = ((newImageWidth * mActivityFragment.getImageHeight()) / mActivityFragment.getImageWidth());
//        tempImageView.setMinimumHeight(newImageHeight);
//        tempImageView.setMinimumWidth(newImageWidth);
//        if ((posterURL == null) || (posterURL.isEmpty()) || (posterURL.equals("null"))) {
//            tempImageView.setImageResource(R.drawable.no_image_thumb);
//        } else {
//            Glide.with(mContext)
//                    .load(mBaseImgStr + posterURL)
//                    .override(newImageWidth,newImageHeight)
//                    .into((ImageView) (convertView.findViewById(R.id.movie_poster)));
//        }
//        View popupButton = convertView.findViewById(R.id.popup_button);
//        popupButton.setTag(movieTitle);
//        popupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                v.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mActivityFragment.showPopupMenu(v);
//                    }
//                });
//            }
//        });
//        return convertView;
//    }


    @Override
    public MovieParcel.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_layout, parent, false);
        view.setOnClickListener(this);
        return new MovieParcel.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieParcel.ViewHolder holder, int position) {
        MovieParcel movieParcel = movieParcelList.get(position);
        String movieTitle = movieParcel.getMovieTitle();
        holder.movieTitle.setText(movieTitle);

        //for movie poster
        String posterURL = movieParcel.getImagePosterURL();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = mActivityFragment.recyclerView.getWidth();

        // eventual width of each image
        int newImageWidth = (screenWidth / mActivityFragment.getNumGridViewCols());

        // original dimensions : mImageWidth and mImageHeight
        int newImageHeight = ((newImageWidth * mActivityFragment.getImageHeight()) / mActivityFragment.getImageWidth());
        holder.moviePoster.setMinimumHeight(newImageHeight);
        holder.moviePoster.setMinimumWidth(newImageWidth);
        if ((posterURL == null) || (posterURL.isEmpty()) || (posterURL.equals("null"))) {
            holder.moviePoster.setImageResource(R.drawable.no_image_thumb);
        } else {
            Glide.with(mContext)
                    .load(mBaseImgStr + posterURL)
                    .override(newImageWidth,newImageHeight)
                    .into(holder.moviePoster);
        }
    //for rating
        double rating = movieParcel.getUserRating();
        holder.movieRating.setText(String.valueOf(rating));
        //setting the rating on the rating bar
        holder.movieRatingBar.setRating((float) rating);

        //overflow button for favourite
        holder.popupButton.setTag(position);
        holder.popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragment.showPopupMenu(v);
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return movieParcelList.size();
    }

    @Override
    public void onClick(View v) {
        int position = mActivityFragment.recyclerView.getChildLayoutPosition(v);
        MovieParcel movieParcel = movieParcelList.get(position);
        if(MoviesActivity.mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable("movieParcel",movieParcel);

            MovieDetailActivityFragment mdFragmnent = new MovieDetailActivityFragment();
            mdFragmnent.setArguments(args);

            ((MoviesActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,mdFragmnent)
                    .commit();;
        }
        else {
            Intent intent = new Intent(mContext, MovieDetailActivity.class);
////        intent.putExtra("movieTitle",movieParcel.getMovieTitle());
////        intent.putExtra("backdropURL",movieParcel.getBackdropURL());
            intent.putExtra("movieParcel", movieParcel);
            mContext.startActivity(intent);
        }
    }
}
