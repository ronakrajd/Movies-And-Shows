package com.android.ronakdoongarwal.moviesandshows;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
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


public class MoviesFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, ObservableScrollViewCallbacks {
    private int mImageWidth;
    private int mImageHeight;
    protected static Spinner s=null;
    public static String sortByParam;
    private DisplayMetrics mDisplayMetrics = new DisplayMetrics();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.sort_menu, menu); // inflate the menu
        s = (Spinner) menu.findItem(R.id.sort_menu_spinner).getActionView();     // find the spinner
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_menu_spinner_list, android.R.layout.simple_spinner_dropdown_item);    // create the adapter from a StringArray
        s.setAdapter(mSpinnerAdapter);   // set the adapter
        s.setOnItemSelectedListener(this);    // (optional) reference to a OnItemSelectedListener, that you can use to perform actions based on user selection
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).toString().equals("Popular")){
            sortByParam="popular";
            GetMoviesTask fetchMovies = new GetMoviesTask(this, getActivity());
            fetchMovies.execute();
        }
        if(parent.getItemAtPosition(position).toString().equals("Top Rated")){
            sortByParam="top_rated";
            GetMoviesTask fetchMovies = new GetMoviesTask(this, getActivity());
            fetchMovies.execute();
        }
        if(parent.getItemAtPosition(position).toString().equals("In theatres")){
            sortByParam="now_playing";
            GetMoviesTask fetchMovies = new GetMoviesTask(this,getActivity());
            fetchMovies.execute();
        }
        if(parent.getItemAtPosition(position).toString().equals("Coming Soon")){
            sortByParam="upcoming";
            GetMoviesTask fetchMovies = new GetMoviesTask(this, getActivity());
            fetchMovies.execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private List<MovieParcel> movieList = new ArrayList<MovieParcel>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private MovieAdapter mMovieListAdapter;
    private ObservableGridView mGridView ;

    public MoviesFragment() {
    }

    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = (ObservableGridView) rootView.findViewById(R.id.gridView);
        mGridView.setScrollViewCallbacks(this);
        return rootView;
    }
    public void addToList(MovieParcel obj){
        movieList.add(obj);
    }
    public void clearMovieList(){
        movieList.clear();
    }

    public void initUI() {
        mMovieListAdapter = new MovieAdapter(getActivity(), this, movieList);
        mGridView.setAdapter(mMovieListAdapter);
        GetMoviesTask.dialog.dismiss();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieParcel tempParcel = mMovieListAdapter.getItem(position);
                Toast.makeText(getContext(),tempParcel.getMovieTitle(),Toast.LENGTH_LONG).show();
            }
        });
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
        return mGridView.getNumColumns();
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
        final String item = (String) view.getTag();

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
                        Toast.makeText(getContext(),"Working on the feature...",Toast.LENGTH_LONG).show();
                        return true;
                }
                return false;
            }
        });

        // Finally show the PopupMenu
        popup.show();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}