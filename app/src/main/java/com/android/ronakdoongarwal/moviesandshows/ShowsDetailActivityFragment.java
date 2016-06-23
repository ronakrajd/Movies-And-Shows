package com.android.ronakdoongarwal.moviesandshows;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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
public class ShowsDetailActivityFragment extends Fragment implements View.OnClickListener {
    public static ArrayList<String> videoPath = new ArrayList<>();
    public static ArrayList<String> videoType = new ArrayList<>();
    public static List<Review>  review = new ArrayList<>();
    View view;
    public ShowsDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_shows_detail, container, false);
        TVShowParcel showParcel = getActivity().getIntent().getParcelableExtra("showParcel");
//        Log.d("posterurl", ""+(getResources().getString(R.string.api_base_image_url)+getActivity().getIntent().getCharSequenceExtra("posterURL")));
        ImageView imageView  = (ImageView) view.findViewById(R.id.posterImage);
        Glide.with(this)
                .load(getResources().getString(R.string.api_base_detail_image_url)+showParcel.getImagePosterURL())
                .override(154,200)
                .into(imageView);
        ((TextView)view.findViewById(R.id.dshow_title)).setText(showParcel.getTVShowTitle());
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.detail_acitvity_rating_bar);
        ratingBar.setRating((float) showParcel.getUserRating());
        ((TextView)view.findViewById(R.id.rating)).setText(String.valueOf(showParcel.getUserRating()));
        ((TextView)view.findViewById(R.id.release_date)).setText(showParcel.getReleaseDate());
        ((TextView)view.findViewById(R.id.overview_text)).setText(showParcel.getOverview());
        ((TextView)view.findViewById(R.id.vote_count)).setText(showParcel.getVoteCount());
        GetShowVideos fetchVideos = new GetShowVideos(this,showParcel.getShowId());
        fetchVideos.execute();
        GetShowReviews fetchReviews = new GetShowReviews(this,showParcel.getShowId());
        fetchReviews.execute();
        return  view;
    }

    @Override
    public void onClick(View v) {
        int position  = (int) v.getTag();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+videoPath.get(position))));
    }

    public static class GetShowVideos extends AsyncTask<Void, Void, Void> {
        private int showId;
        private ShowsDetailActivityFragment mdFragment;

        public GetShowVideos(ShowsDetailActivityFragment mfragment, int showId) {
        this.mdFragment = mfragment;
        this.showId = showId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;
        try {
            videoPath.clear();
            videoType.clear();
            try {
                Uri.Builder builder = new Uri.Builder();
                String key = "e7684f27608b0d0d83be08309126045d";
                builder.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("tv")
                        .appendPath("" + showId)
                        .appendPath("videos")
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
        JSONObject showVideoObject = null;
        JSONArray resultArray = null;
        String path, type;
        try {
            if (jsonStr == null) {
                return null;
            }
            JSONObject json = new JSONObject(jsonStr);
            resultArray = json.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                showVideoObject = resultArray.getJSONObject(i);
                videoPath.add(showVideoObject.getString("key"));
                videoType.add(showVideoObject.getString("type"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mdFragment.populateVideos();
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
            view.setOnClickListener(this);
        }
    }
    public static class GetShowReviews extends AsyncTask<Void, Void, Void> {
        private int showId;
        private ShowsDetailActivityFragment mdFragment;

        public GetShowReviews(ShowsDetailActivityFragment mfragment, int showId) {
            this.mdFragment = mfragment;
            this.showId = showId;
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
                            .appendPath("tv")
                            .appendPath("" + showId)
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
            JSONObject showReviewObject = null;
            String author,content;
            JSONArray resultArray = null;
            try {
                if (jsonStr == null) {
                    return null;
                }
                JSONObject json = new JSONObject(jsonStr);
                resultArray = json.getJSONArray("results");
                for (int i = 0; i < resultArray.length(); i++) {
                    showReviewObject = resultArray.getJSONObject(i);
                    author = showReviewObject.getString("author");
                    content = showReviewObject.getString("content");
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
