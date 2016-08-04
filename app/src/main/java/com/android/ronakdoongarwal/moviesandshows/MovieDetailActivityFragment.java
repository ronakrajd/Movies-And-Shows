package com.android.ronakdoongarwal.moviesandshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    private static String runTime;
    private static ArrayList<MovieGenre> genresList = new ArrayList<>();
    public static ArrayList<String> videoPath = new ArrayList<>();
    public static ArrayList<String> videoType = new ArrayList<>();
    public static ArrayList<String> author = new ArrayList<>();
    public static List<Review> review = new ArrayList<>();
    private static String homepage;
    View view;
    public MovieDetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Bundle args = getArguments();
        final MovieParcel movieParcel;
        Search.searchRes=false;
        if(args!=null) {

            movieParcel = args.getParcelable("movieParcel");
            GetMovieDetail fetchDetail = new GetMovieDetail(this, movieParcel.getmMovieId());
            fetchDetail.execute();
            GetMovieReviews fetchReviews = new GetMovieReviews(this, movieParcel.getmMovieId());
            fetchReviews.execute();
            ImageView imageView = (ImageView) view.findViewById(R.id.posterImage);
            Glide.with(this)
                    .load(getResources().getString(R.string.api_base_detail_image_url) + movieParcel.getImagePosterURL())
                    .override(154, 200)
                    .into(imageView);
            ((TextView) view.findViewById(R.id.dshow_title)).setText(movieParcel.getMovieTitle());
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.detail_acitvity_rating_bar);
            ratingBar.setRating((float) movieParcel.getUserRating());
            ((TextView) view.findViewById(R.id.rating)).setText(String.valueOf(movieParcel.getUserRating()));
            ((TextView) view.findViewById(R.id.release_date)).setText(movieParcel.getReleaseDate());
            ((TextView) view.findViewById(R.id.overview_text)).setText(movieParcel.getOverview());
            ((TextView) view.findViewById(R.id.vote_count)).setText(movieParcel.getVoteCount());
            ((TextView) view.findViewById(R.id.run_time)).setText(runTime);

            if(view.findViewById(R.id.favorite_button)!=null){
                Button fav = (Button) view.findViewById(R.id.favorite_button);
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FavoriteMovieDBHelper favoriteMovie = new FavoriteMovieDBHelper(getContext());
                        favoriteMovie.addMovieToFavorite(movieParcel);
                        Snackbar.make(view, "Movie added to favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        }
        return view;
    }





    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void updateUI(){
        final LinearLayout genreList = (LinearLayout) view.findViewById(R.id.genreLabels);
        for(int i=0;i<genresList.size();i++) {
            TextView genre = new TextView(getContext());
            genre.setTag(i);
            genre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position= (int) v.getTag();
                    Intent intent = new Intent(getContext(),Search.class);
                    intent.putExtra("searchStr","with_genres");
                    String id = String.valueOf(genresList.get(position).getGenreId());
                    intent.putExtra("value",id);
                    intent.putExtra("Title","Results for "+genresList.get(position).getGenre()+" movies");
                    startActivity(intent);
                }
            });
            genre.setText(genresList.get(i).getGenre());
            genre.setBackground(getResources().getDrawable(R.drawable.label_bg));
            genre.setTextAppearance(getContext(),R.style.labelTextAppearance);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                params.setMargins(0,0,15,0);
                genre.setLayoutParams(params);
            }
            genreList.addView(genre);
        }
        Button button = (Button) view.findViewById(R.id.homepage_button);
        if(homepage!=null&&(homepage.contains("http://")||homepage.contains("https://"))){

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(homepage);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        else{
            button.setVisibility(View.GONE);
        }

    }

    public static class GetMovieDetail extends AsyncTask<Void, Void, Void> {
        private int movieId;
        private MovieDetailActivityFragment mdFragment;



        public GetMovieDetail(MovieDetailActivityFragment mfragment, int movieId) {
            this.mdFragment = mfragment;
            this.movieId = movieId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;
            try {
                videoPath.clear();
                videoType.clear();
                genresList.clear();
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String key = "e7684f27608b0d0d83be08309126045d";
                    builder.scheme("https")
                            .authority("api.themoviedb.org")
                            .appendPath("3")
                            .appendPath("movie")
                            .appendPath("" + movieId)
                            .appendQueryParameter("api_key", key)
                            .appendQueryParameter("append_to_response","trailers,credits");
                    String strURL = builder.build().toString();
                    Log.d("url", strURL);
                    URL url = new URL(strURL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null)
                        jsonStr = null;
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        jsonStr = null;
                    }
                    jsonStr = stringBuffer.toString();
                } catch (MalformedURLException e) {
                    Log.e("GetMoviesDetails", "Errot", e);
                }
            } catch (IOException e) {
                Log.e("GetMoviesDetails", "doInBackground: ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            JSONObject movieVideoObject = null;
            JSONArray resultArray = null;
            String path, type;
            try {
                if (jsonStr == null) {
                    return null;
                }

                JSONObject json = new JSONObject(jsonStr);
                runTime  = String.format("%02d",json.getInt("runtime")/60)+" Hr "+String.format("%02d",(json.getInt("runtime")%60))+" Min";
                homepage = json.getString("homepage");
                resultArray = json.getJSONObject("trailers").getJSONArray("youtube");
                for (int i = 0; i < resultArray.length(); i++) {
                    movieVideoObject = resultArray.getJSONObject(i);
                    videoPath.add(movieVideoObject.getString("source"));
                    videoType.add(movieVideoObject.getString("type"));
                }
                resultArray = json.getJSONArray("genres");
                for (int i = 0; i < resultArray.length(); i++) {
                    movieVideoObject = resultArray.getJSONObject(i);
                    genresList.add(new MovieGenre(movieVideoObject.getInt("id"),movieVideoObject.getString("name")));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mdFragment.populateVideos();
            mdFragment.updateUI();
        }
    }

    private void populateVideos() {
        LinearLayout videoListLayout = (LinearLayout) view.findViewById(R.id.video_list);
        for (int i=0;i<videoPath.size();i++){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.video_item, videoListLayout,false);
            Glide.with(getContext()).load("http://img.youtube.com/vi/"+videoPath.get(i)+"/0.jpg").into((ImageView)view.findViewById(R.id.video_thumb_image));
            ((TextView)view.findViewById(R.id.video_type)).setText(videoType.get(i));
            videoListLayout.addView(view);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position  = (int) v.getTag();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+videoPath.get(position))));
                }
            });
        }
    }

    public static class GetMovieReviews extends AsyncTask<Void, Void, Void> {
        private int movieId;
        private MovieDetailActivityFragment mdFragment;

        public GetMovieReviews(MovieDetailActivityFragment mfragment, int movieId) {
            this.mdFragment = mfragment;
            this.movieId = movieId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;
            try {
                review.clear();
                try {
                    Uri.Builder builder = new Uri.Builder();
                    String key = "e7684f27608b0d0d83be08309126045d";
                    builder.scheme("https")
                            .authority("api.themoviedb.org")
                            .appendPath("3")
                            .appendPath("movie")
                            .appendPath("" + movieId)
                            .appendPath("reviews")
                            .appendQueryParameter("api_key", key);
                    String strURL = builder.build().toString();
                    Log.d("url", strURL);
                    URL url = new URL(strURL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null)
                        jsonStr = null;
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }
                    if (stringBuffer.length() == 0) {
                        jsonStr = null;
                    }
                    jsonStr = stringBuffer.toString();
                } catch (MalformedURLException e) {

                }
            } catch (IOException e) {

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            JSONObject movieReviewObject = null;
            String author,content;
            JSONArray resultArray = null;
            try {
                if (jsonStr == null) {
                    return null;
                }
                JSONObject json = new JSONObject(jsonStr);
                resultArray = json.getJSONArray("results");
                for (int i = 0; i < resultArray.length(); i++) {
                    movieReviewObject = resultArray.getJSONObject(i);
                    author = movieReviewObject.getString("author");
                    content = movieReviewObject.getString("content");
                    Review r = new Review(author,content);
                    review.add(r);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mdFragment.populateReviews();
        }
    }

    private void populateReviews() {
        if(review.size()==0){
            Review r = new Review("No Reviews Available","");
            review.add(r);
        }
        ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(),this,review);
        ListView listView = (ListView) view.findViewById(R.id.reviews_list);
        listView.setAdapter(reviewAdapter);
    }
}