package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_trailer);
        ActivityMovieTrailerBinding binding = ActivityMovieTrailerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String videoKey = intent.getStringExtra("videoId");
        final String videoId = videoKey;

        YouTubePlayerView playerView = (YouTubePlayerView) binding.player;


        playerView.initialize(getString(R.string.yt_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setFullscreen(true);
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}
