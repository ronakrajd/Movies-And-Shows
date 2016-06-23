package com.android.ronakdoongarwal.moviesandshows;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TVShowParcel showParcel = getIntent().getParcelableExtra("showParcel");
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(showParcel.getTVShowTitle());

        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(getResources().getString(R.string.api_backdrop_url)+showParcel.getBackdropURL()).centerCrop().into(imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteShowDBHelper db = new FavoriteShowDBHelper(getApplicationContext());
                db.addShowToFavorite(showParcel);
                Snackbar.make(view, "Show is added to favorites", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
