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

public class GetShowsTask extends AsyncTask<Void, Void, Void> {
    public static ProgressDialog dialog;
    private TVShowsFragment tvShowsFragment= null;
    public GetShowsTask(TVShowsFragment tvShowsFragment, FragmentActivity activity) {
        this.tvShowsFragment=tvShowsFragment;
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
            tvShowsFragment.clearShowList();
            try {
                Uri.Builder builder = new Uri.Builder();
                String key ="e7684f27608b0d0d83be08309126045d";
                builder.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("tv")
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
                Log.e("GetTVShowsTask", "Errot", e);
            }
        } catch (IOException e) {
            Log.e("GetTVShowsTask", "doInBackground: ", e);
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
        JSONObject showDetailObject=null;
        JSONArray resultArray=null;
        int totalPages;
        String showTitle, posterURL, overView, releaseDate;
        double userRating;
        try{
            if (jsonStr==null) {
                return null;
            }
            JSONObject json = new JSONObject(jsonStr);
            resultArray = json.getJSONArray("results");
            totalPages = Integer.parseInt(json.getString("total_pages"));
            for(int i=0;i<resultArray.length();i++){
                showDetailObject=resultArray.getJSONObject(i);
                showTitle=showDetailObject.getString("name");
                posterURL=showDetailObject.getString("poster_path");
                overView=showDetailObject.getString("overview");
                userRating=showDetailObject.getDouble("vote_average");
                releaseDate=showDetailObject.getString("first_air_date");
                    Log.d("detailadf", showTitle);
                TVShowParcel tempParcel = new TVShowParcel(showTitle,
                        overView,
                        releaseDate,
                        posterURL,
                        userRating);
                tvShowsFragment.addToList(tempParcel);
            }
            Bitmap image;

            // find the first posterURL that is not null
            for(int i = 0; i < TVShowsFragment.getShowsListSize(); i++) {

                String posterURL1 = TVShowsFragment.getPosterURL(i);

                if((posterURL1 != null) && (!posterURL1.isEmpty()) && (!posterURL1.equals("null"))) {
                    // load an image to retrieve the dimensions
                    try {
                        image = Glide
                                .with(tvShowsFragment.getActivity())
                                .load(tvShowsFragment.getString(R.string.api_base_image_url) + TVShowsFragment.getPosterURL(i))
                                .asBitmap()
                                .into(-1,-1)
                                .get();
                        tvShowsFragment.setImageDimensions(image.getWidth(), image.getHeight());
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

        tvShowsFragment.initUI();
    }
}
