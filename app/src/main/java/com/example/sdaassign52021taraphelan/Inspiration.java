package com.example.sdaassign52021taraphelan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleObserver;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import static android.view.View.GONE;

public class Inspiration extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private final String TAG = "Inspiration";

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

        //hides toolbar if activity is opened in landscape mode
        if (getResources().getConfiguration().orientation == 2) {
            getSupportActionBar().hide();
        }

        //YouTube video player tutorial found at https://www.youtube.com/watch?v=yyduqrCpKGg
        youTubePlayerView = findViewById(R.id.youtubePlayer);
        //getLifecycle().addObserver(youTubePlayerView);

        //hiding the toolbar when fullscreen is entered and showing it again on exit
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                getSupportActionBar().hide();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                if (getResources().getConfiguration().orientation == 1) {
                    getSupportActionBar().show();
                }
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String videoId = "8KkKuTCFvzI";
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
}