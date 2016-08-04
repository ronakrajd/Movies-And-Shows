package com.android.ronakdoongarwal.moviesandshows;

import android.app.FragmentTransaction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Search extends AppCompatActivity {

    public static boolean searchRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            MoviesFragment moviesFragment = new MoviesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_container,moviesFragment)
                    .commit();
            Intent intent = getIntent();
            searchRes = true;
        getSupportActionBar().setTitle(intent.getStringExtra("Title"));

                GetMoviesTask fetchMovies = new GetMoviesTask(moviesFragment, Search.this, intent.getStringExtra("searchStr"), intent.getStringExtra("value"));
                fetchMovies.execute();

        }

    }
}
