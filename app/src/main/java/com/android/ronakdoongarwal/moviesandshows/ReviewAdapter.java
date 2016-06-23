package com.android.ronakdoongarwal.moviesandshows;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 6/23/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review>{
    Context context;
    MovieDetailActivityFragment mFragment;
    List<Review> reviewsList;
    public ReviewAdapter(Context context, MovieDetailActivityFragment movieDetailActivityFragment, List<Review> review) {
        super(context,0,review);
        this.context = context;
        this.reviewsList = review;
    }

    public ReviewAdapter(Context context, ShowsDetailActivityFragment showsDetailActivityFragment, List<Review> review) {
        super(context, 0, review);
        this.context = context;
        this.reviewsList = review;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item,parent,false);
        }
        ((TextView)convertView.findViewById(R.id.author)).setText(review.getReviewAuthor());
        ((TextView)convertView.findViewById(R.id.review_text)).setText(review.getReviewContent());
        return convertView;
    }
}
