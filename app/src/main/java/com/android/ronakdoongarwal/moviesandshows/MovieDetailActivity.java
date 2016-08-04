package com.android.ronakdoongarwal.moviesandshows;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(savedInstanceState==null){
            Bundle args = new Bundle();
            args.putParcelable("movieParcel",getIntent().getParcelableExtra("movieParcel"));
            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container,fragment)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //enabling the back to parent arrow in the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setSubtitle("Movie Tagline Here");

        final MovieParcel movieParcel = getIntent().getParcelableExtra("movieParcel");
        //loading the backdrop poster in the imageview inside collasping toolbar
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(getResources().getString(R.string.api_backdrop_url)+movieParcel.getBackdropURL()).centerCrop().into(imageView);

        //setting the title of the movie on the t6oolbar
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movieParcel.getMovieTitle());

        //performing the favorite action on floating action button of favorite
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteMovieDBHelper favoriteMovie = new FavoriteMovieDBHelper(getApplicationContext());
                favoriteMovie.addMovieToFavorite(movieParcel);
                Snackbar.make(view, "Movie added to favorites", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
