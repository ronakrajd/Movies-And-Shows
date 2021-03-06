package com.android.ronakdoongarwal.moviesandshows;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
import java.util.concurrent.ExecutionException;

public class GetMoviesTask extends AsyncTask<Void, Void, Void> {
    public static ProgressDialog dialog;
    private MoviesFragment moviesFragment= null;
    public GetMoviesTask(MoviesFragment moviesFragment, FragmentActivity activity) {
        this.moviesFragment=moviesFragment;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Please Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;
        try {
            moviesFragment.clearMovieList();
            try {
                Uri.Builder builder = new Uri.Builder();
                String key ="e7684f27608b0d0d83be08309126045d";
                builder.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(MoviesFragment.sortByParam)
                        .appendQueryParameter("api_key", key );
                String strURL = builder.build().toString();
                Log.d("url",strURL);
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
                Log.e("GetMoviesTask", "Errot", e);
            }
        } catch (IOException e) {
            Log.e("GetMoviesTask", "doInBackground: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject movieDetailObject=null;
        JSONArray resultArray=null;
        int totalPages;
        String movieTitle, posterURL, overView, releaseDate;
        double userRating;
        try{
            if (jsonStr==null) {
                return null;
            }
            JSONObject json = new JSONObject(jsonStr);
            resultArray = json.getJSONArray("results");
            totalPages = Integer.parseInt(json.getString("total_pages"));
            for(int i=0;i<resultArray.length();i++){
                movieDetailObject=resultArray.getJSONObject(i);
                movieTitle=movieDetailObject.getString("title");
                posterURL=movieDetailObject.getString("poster_path");
                overView=movieDetailObject.getString("overview");
                userRating=movieDetailObject.getDouble("vote_average");
                releaseDate=movieDetailObject.getString("release_date");
                    Log.d("detailadf", movieTitle);
                MovieParcel tempParcel = new MovieParcel(movieTitle,
                        overView,
                        releaseDate,
                        posterURL,
                        userRating);
                moviesFragment.addToList(tempParcel);
            }
            Bitmap image;

                // find the first posterURL that is not null
                for(int i = 0; i < moviesFragment.getMovieListSize(); i++) {

                    String posterURL1 = moviesFragment.getPosterURL(i);

                    if((posterURL1 != null) && (!posterURL1.isEmpty()) && (!posterURL1.equals("null"))) {
                        // load an image to retrieve the dimensions
                        try {
                            image = Glide
                                    .with(moviesFragment.getActivity())
                                    .load(moviesFragment.getString(R.string.api_base_image_url) + moviesFragment.getPosterURL(i))
                                    .asBitmap()
                                    .into(-1,-1)
                                    .get();
                            moviesFragment.setImageDimensions(image.getWidth(), image.getHeight());
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        moviesFragment.initUI();
    }
}
