package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public final String TAG = "MovieDetailsActivity";

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView backdrop;
    String videoKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        backdrop = findViewById(R.id.imageView);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        rbVoteAverage.setRating(movie.getVoteAverage().floatValue());
        Glide.with(this).load(movie.getBackdropPath()).into(backdrop);

        AsyncHttpClient client = new AsyncHttpClient();
        String key = getString(R.string.api_key);
        final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/" + movie.id + "/videos?api_key=" + key;
        Log.d(TAG, VIDEOS_URL);
        Log.d(TAG, movie.id.toString());
        client.get(VIDEOS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject result = (JSONObject) results.get(0);
                    videoKey = result.getString("key");
                } catch (JSONException e){
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure", throwable);
            }
        });

        backdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MovieTrailerActivity.class);
                intent.putExtra("videoId", videoKey);
                startActivity(intent);
            }
        });
    }
}
