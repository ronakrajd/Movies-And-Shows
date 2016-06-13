package com.android.ronakdoongarwal.moviesandshows;

import android.content.Context;
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
public class MovieAdapter extends ArrayAdapter<MovieParcel> {
    private Context mContext;
    private MoviesFragment mActivityFragment;
    private String mBaseImgStr = "";

    public MovieAdapter(Context context, MoviesFragment activityFragment, List<MovieParcel> imageUrls) {
        super(context, R.layout.movie_grid_layout, imageUrls);
        Log.e("crash", "MovieAdapter: " );
        this.mContext = context;
        this.mActivityFragment = activityFragment;
        this.mBaseImgStr = mActivityFragment.getString(R.string.api_base_image_url);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieParcel movieParcel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_layout, parent, false);
        }
        ImageView tempImageView = (ImageView) (convertView.findViewById(R.id.movie_poster));
        String posterURL = movieParcel.getImagePosterURL();
        Log.e("crash error", "getView: ");
        String movieTitle = movieParcel.getMovieTitle();
        TextView movie_tv = (TextView) convertView.findViewById(R.id.movie_title);
        movie_tv.setText(movieTitle);

        TextView user_rating = (TextView) convertView.findViewById(R.id.movie_rating);
        double rating = movieParcel.getUserRating();
        user_rating.setText(String.valueOf(rating));
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        ratingBar.setStepSize(0.001f);
        ratingBar.setRating((float) rating);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;

        // eventual width of each image
        int newImageWidth = (screenWidth / mActivityFragment.getNumGridViewCols());

        // original dimensions : mImageWidth and mImageHeight
        int newImageHeight = ((newImageWidth * mActivityFragment.getImageHeight()) / mActivityFragment.getImageWidth());
        tempImageView.setMinimumHeight(newImageHeight);
        tempImageView.setMinimumWidth(newImageWidth);
        if ((posterURL == null) || (posterURL.isEmpty()) || (posterURL.equals("null"))) {
            tempImageView.setImageResource(R.drawable.no_image_thumb);
        } else {
            Glide.with(mContext)
                    .load(mBaseImgStr + posterURL)
                    .override(newImageWidth,newImageHeight)
                    .into((ImageView) (convertView.findViewById(R.id.movie_poster)));
        }
        View popupButton = convertView.findViewById(R.id.popup_button);
        popupButton.setTag(movieTitle);
        popupButton.setOnClickListener(new View.OnClickListener() {
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
        return convertView;
    }
}
