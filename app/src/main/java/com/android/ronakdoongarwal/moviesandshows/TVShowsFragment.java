package com.android.ronakdoongarwal.moviesandshows;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;


public class TVShowsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    protected static Spinner s=null;
    private int mImageWidth;
    private int mImageHeight;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.sort_menu, menu); // inflate the menu
        s = (Spinner) menu.findItem(R.id.sort_menu_spinner).getActionView();     // find the spinner

        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort_menu_spinner_list2, android.R.layout.simple_spinner_dropdown_item);    // create the adapter from a StringArray

        s.setAdapter(mSpinnerAdapter);   // set the adapter
        s.setOnItemSelectedListener(this);    // (optional) reference to a OnItemSelectedListener, that you can use to perform actions based on user selection
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).toString().equals("Popular")){
            MoviesFragment.sortByParam="popular";
            GetShowsTask fetchShows = new GetShowsTask(this, getActivity());
            fetchShows.execute();
        }
        if(parent.getItemAtPosition(position).toString().equals("Top Rated")){
            MoviesFragment.sortByParam="top_rated";
            GetShowsTask fetchShows = new GetShowsTask(this, getActivity());
            fetchShows.execute();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static List<TVShowParcel> showList = new ArrayList<TVShowParcel>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TVShowAdapter mShowListAdapter;
    private RecyclerView recyclerView;

    public TVShowsFragment() {
    }

    public static TVShowsFragment newInstance() {
        TVShowsFragment fragment = new TVShowsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_tvshows,container,false);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        return recyclerView;
    }
    public void addToList(TVShowParcel obj){
        showList.add(obj);
    }
    public void clearShowList(){
        showList.clear();
    }

    public void initUI() {
        Log.d("debugging", "initUI: ");
        mShowListAdapter = new TVShowAdapter(getActivity(), this, showList);
        recyclerView.setAdapter(mShowListAdapter);
        GetShowsTask.dialog.dismiss();
    }

    public static int getShowsListSize() {
        return showList.size();
    }

    public static String getPosterURL(int i) {
        return showList.get(i).getImagePosterURL();
    }
    public void setImageDimensions(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
    }

    public int getNumGridViewCols() {
        return 2;    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public int getImageWidth() {
        return mImageWidth;
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
}
