package com.android.ronakdoongarwal.moviesandshows;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.List;


public class MoviesFragment extends Fragment implements View.OnClickListener {
    private int mImageWidth;
    private int mImageHeight;
    private List<MovieParcel> movieList = new ArrayList<MovieParcel>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private MovieAdapter mMovieListAdapter;
    public static String sortByParam;
    private DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    public  RecyclerView recyclerView;
    private boolean load=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            movieList = savedInstanceState.getParcelableArrayList("movieList");
            load = false;
            Toast.makeText(getActivity(),"Restored the list",Toast.LENGTH_LONG).show();
        }
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_movies,container,false);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        if(!Search.searchRes)
        itemSelected("Popular");
        return recyclerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieList", (ArrayList<? extends Parcelable>) movieList);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("ViewDestroyed", "onDestroyView: ");
    }


    public void itemSelected(String selectedItem) {
           if(!load)
               return;
            if (selectedItem.equals("Favorites")) {
                // Log.d("favorite","Inside favorite" );
                FavoriteMovieDBHelper db = new FavoriteMovieDBHelper(getContext());
                movieList.clear();
                movieList = db.getFavoriteMovieList();
                initUI(getContext());
            }
            if (selectedItem.equals("Popular")) {
                sortByParam = "popular";
                GetMoviesTask fetchMovies = new GetMoviesTask(this,getContext());
                fetchMovies.execute();
            }
            if (selectedItem.equals("Top Rated")) {
                sortByParam = "top_rated";
                GetMoviesTask fetchMovies = new GetMoviesTask(this, getActivity());
                fetchMovies.execute();
            }
            if (selectedItem.equals("In theatres")) {
                sortByParam = "now_playing";
                GetMoviesTask fetchMovies = new GetMoviesTask(this, getActivity());
                fetchMovies.execute();
            }
            if (selectedItem.equals("Coming Soon")) {
                sortByParam = "upcoming";
                GetMoviesTask fetchMovies = new GetMoviesTask(this, getActivity());
                fetchMovies.execute();
            }

    }






    public MoviesFragment() {
        movieList.clear();
    }

    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        return fragment;
    }


    public void addToList(MovieParcel obj){
        movieList.add(obj);
    }
    public void clearMovieList(){
        movieList.clear();
    }

    public void initUI(Context context) {
        mMovieListAdapter = new MovieAdapter(context, this, movieList);
        recyclerView.setAdapter(mMovieListAdapter);
        GetMoviesTask.dialog.dismiss();
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                MovieParcel tempParcel = mMovieListAdapter.getItem(position);
//                Toast.makeText(getContext(),tempParcel.getMovieTitle(),Toast.LENGTH_LONG).show();
//            }
//        });
    }
    @Override
    public void onClick(final View view) {
        // We need to post a Runnable to show the popup to make sure that the PopupMenu is
        // correctly positioned. The reason being that the view may change position before the
        // PopupMenu is shown.
        view.post(new Runnable() {
            @Override
            public void run() {
//                showPopupMenu(view);
                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void setImageDimensions(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
    }

    public String getPosterURL(int i) {
        return movieList.get(i).getImagePosterURL();
    }

    public int getMovieListSize() {
        return movieList.size();
    }



    public int getNumGridViewCols() {
        return 2;
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public int getImageWidth() {
        return mImageWidth;
    }
    public int getDisplayMetricsWidth() {
        return mDisplayMetrics.widthPixels;
    }

    public void showPopupMenu(View view) {
        // Retrieve the clicked item from view's tag
        final int position = (int) view.getTag();

        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(getActivity(), view);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_favorite:
                        FavoriteMovieDBHelper favoriteMovie = new FavoriteMovieDBHelper(getContext());
                        favoriteMovie.addMovieToFavorite(movieList.get(position));
                        return true;
                }
                return false;
            }
        });

        // Finally show the PopupMenu
        popup.show();
    }




}