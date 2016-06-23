package com.android.ronakdoongarwal.moviesandshows;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
public class TVShowAdapter extends RecyclerView.Adapter<TVShowParcel.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private TVShowsFragment mActivityFragment;
    private String mBaseImgStr = "";
    public List<TVShowParcel> showParcels;

    public TVShowAdapter(Context context, TVShowsFragment activityFragment, List<TVShowParcel> showParcelList) {
        //super(context, R.layout.movie_grid_layout, imageUrls);
        this.mContext = context;
        this.mActivityFragment = activityFragment;
        this.mBaseImgStr = mActivityFragment.getString(R.string.api_base_image_url);
        this.showParcels=showParcelList;
    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TVShowParcel tvShowParcel = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_layout, parent, false);
//        }
//        ImageView tempImageView = (ImageView) (convertView.findViewById(R.id.movie_poster));
//        String posterURL = tvShowParcel.getImagePosterURL();
//
//        String tvShowTitle = tvShowParcel.getTVShowTitle();
//        TextView tvShow_tv = (TextView) convertView.findViewById(R.id.movie_title);
//
//        double rating = tvShowParcel.getUserRating();
//        TextView user_rating = (TextView) convertView.findViewById(R.id.movie_rating);
//        user_rating.setText(String.valueOf(rating));
//        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
//        ratingBar.setStepSize(0.001f);
//        ratingBar.setRating((float) rating);
//
//        tvShow_tv.setText(tvShowTitle);DisplayMetrics displaymetrics = new DisplayMetrics();
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
//        return convertView;
//    }

    @Override
    public TVShowParcel.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_layout, parent, false);
        view.setOnClickListener(this);
        return new TVShowParcel.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVShowParcel.ViewHolder holder, int position) {
        TVShowParcel showParcel = showParcels.get(position);
        String showTitleStr = showParcel.getTVShowTitle();
        holder.showTitle.setText(showTitleStr);

        //for show poster
        String posterURL = showParcel.getImagePosterURL();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;

        // eventual width of each image
        int newImageWidth = (screenWidth / mActivityFragment.getNumGridViewCols());

        // original dimensions : mImageWidth and mImageHeight
        int newImageHeight = ((newImageWidth * mActivityFragment.getImageHeight()) / mActivityFragment.getImageWidth());
        holder.showPoster.setMinimumHeight(newImageHeight);
        holder.showPoster.setMinimumWidth(newImageWidth);
        if ((posterURL == null) || (posterURL.isEmpty()) || (posterURL.equals("null"))) {
            holder.showPoster.setImageResource(R.drawable.no_image_thumb);
        } else {
            Glide.with(mContext)
                    .load(mBaseImgStr + posterURL)
                    .override(newImageWidth,newImageHeight)
                    .into(holder.showPoster);
        }
        //for rating
        double rating = showParcel.getUserRating();
        holder.showRating.setText(String.valueOf(rating));
        //setting the rating on the rating bar
        holder.showRatingBar.setRating((float) rating);

        //overflow button for favourite
        holder.popupButton.setTag(showTitleStr);
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
        return showParcels.size();
    }

    @Override
    public void onClick(View v) {
        int position = TVShowsFragment.recyclerView.getChildLayoutPosition(v);
        TVShowParcel showParcel = showParcels.get(position);
        Intent intent = new Intent(mContext,ShowsDetailActivity.class);
//        intent.putExtra("showTitle",showParcel.getTVShowTitle());
//        intent.putExtra("backdropURL",showParcel.getBackdropURL());
//        intent.putExtra("posterURL",showParcel.getImagePosterURL());
        intent.putExtra("showParcel",showParcel);
        mContext.startActivity(intent);
    }
}
