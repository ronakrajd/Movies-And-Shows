package com.android.ronakdoongarwal.moviesandshows;

import android.app.Activity;
import android.app.TabActivity;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class MoviesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static Activity mcontext;
    private ViewPager mViewPager;
    public static boolean mTwoPane;
    private MoviesFragment mFragmentInstance;
    private TVShowsFragment sFragmentInstance;
    private ActionBar actionBar;
    private String curmcriteria="Popular";
    private String curscriteria="Popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(findViewById(R.id.movie_detail_container)!=null){
            mTwoPane = true;
            if(savedInstanceState ==null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container,new MovieDetailActivityFragment())
                        .commit();
            }
        }
        else{
            mTwoPane=false;

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_popular);
            actionBar.setSubtitle("Popular Movies");
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(position==0){
                        actionBar.setSubtitle(curmcriteria+" Movies");
                    }
                    if(position==1){
                        actionBar.setSubtitle(curscriteria+" Shows");
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(item.getGroupId()==R.id.sort_movies) {
            curmcriteria = item.getTitle().toString();
            mFragmentInstance.itemSelected(item.getTitle().toString());
            actionBar.setSubtitle(item.getTitle().toString()+" Movies");
            mViewPager.setCurrentItem(0);
        }
        if(item.getGroupId()==R.id.sort_shows) {
            curscriteria = item.getTitle().toString();
            sFragmentInstance.itemSelected(item.getTitle().toString());
            actionBar.setSubtitle(item.getTitle().toString()+" Shows");
            mViewPager.setCurrentItem(1);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0: mFragmentInstance = MoviesFragment.newInstance();

                   return mFragmentInstance ;

               case 1: sFragmentInstance = TVShowsFragment.newInstance();
                        return sFragmentInstance;

           }
            return MoviesFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Movies";
                case 1:
                    return "TV Shows";
            }
            return null;
        }

    }

}
