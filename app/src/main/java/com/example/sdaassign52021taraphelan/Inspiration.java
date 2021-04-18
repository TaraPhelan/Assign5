package com.example.sdaassign52021taraphelan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Inspiration extends AppCompatActivity {

    private TextView videoTitleTextView;
    private YouTubePlayerView youTubePlayerView;
    private String videoId;
    private final String TAG = "Inspiration";
    private boolean videoIsFullscreen = false;
    private boolean videoIsDefault = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Inspiration tag working");

        setContentView(R.layout.activity_inspiration);

        //if (getResources().getConfiguration().orientation == 1) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //information on adding activity's logical parent found at https://stackoverflow.com/questions/26651602/display-back-arrow-on-toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();

        String neglectedLifeArea = extras.getString("Neglected Life Area");

        videoTitleTextView = findViewById(R.id.videoTitle);
        videoTitleTextView.setText(String.format(getString(R.string.video_title), neglectedLifeArea));

        //hides toolbar and video title if activity is opened in landscape mode
        if (getResources().getConfiguration().orientation == 2) {
            getSupportActionBar().hide();
            videoTitleTextView.setVisibility(GONE);
        }

        Log.i(TAG, "neglected life area is " + neglectedLifeArea);

        switch (neglectedLifeArea) {
            case "health":
                videoId = getString(R.string.video_health);
                break;
            case "family":
                videoId = getString(R.string.video_family);
                break;
            case "work":
                videoId = getString(R.string.video_work);
                break;
            case "friends":
                videoId = getString(R.string.video_friends);
                break;
            case "learning":
                Log.i(TAG, "learning is the case");
                videoId = getString(R.string.video_learning);
                break;
            case "finances":
                videoId = getString(R.string.video_finances);
                break;
            default:
                Log.i(TAG, "default is the case");
                videoId = getString(R.string.video_default);
                videoIsDefault = true;
                videoTitleTextView.setVisibility(GONE);
        }

        //YouTube video player tutorial found at https://www.youtube.com/watch?v=yyduqrCpKGg
        youTubePlayerView = findViewById(R.id.youtubePlayer);
        //getLifecycle().addObserver(youTubePlayerView);

        //hiding the toolbar and video title when fullscreen is entered and showing it again on exit
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                videoIsFullscreen = true;
                getSupportActionBar().hide();
                videoTitleTextView.setVisibility(GONE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                if (getResources().getConfiguration().orientation == 1) {
                    videoIsFullscreen = false;
                    getSupportActionBar().show();
                }
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "in onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "in onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "in onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "in onStart");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //TODO:put all single-line comments in below format

        // Hides toolbar and video title if orientation changes to landscape
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            videoTitleTextView.setVisibility(GONE);

        // Shows toolbar if orientation changes to portrait, unless video is fullscreen or video is default
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && !videoIsFullscreen && !videoIsDefault){
            getSupportActionBar().show();
            videoTitleTextView.setVisibility(VISIBLE);
        }
    }
}