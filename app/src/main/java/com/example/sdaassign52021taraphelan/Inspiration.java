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

/**
 * Inspiration class
 *
 * @author Tara Phelan 2021
 * @version 1.0
 */
public class Inspiration extends AppCompatActivity {

    // Sets up class-wide variables
    private TextView videoTitleTextView;
    private YouTubePlayerView youTubePlayerView;
    private String videoId;
    private final String TAG = "Inspiration";
    private boolean videoIsFullscreen = false;
    private boolean videoIsDefault = false;

    /**
     * Sets the content to the activity_inspiration XML layout and inflates the UI
     *
     * @param savedInstanceState to hold state information
     * @author Tara Phelan 2021
     * @version 1.0
     * @see androidx.appcompat.app.AppCompatActivity {@link #onCreate(Bundle)}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration);

        // Sets up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Information on adding activity's logical parent found at https://stackoverflow.com/questions/26651602/display-back-arrow-on-toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Gets the neglected life area from the Intent extras
        Bundle extras = getIntent().getExtras();
        String neglectedLifeArea = extras.getString("Neglected Life Area");

        // Sets up the video title
        videoTitleTextView = findViewById(R.id.videoTitle);
        videoTitleTextView.setText(String.format(getString(R.string.video_title), neglectedLifeArea));

        // Hides toolbar and video title if activity is opened in landscape mode
        if (getResources().getConfiguration().orientation == 2) {
            getSupportActionBar().hide();
            videoTitleTextView.setVisibility(GONE);
        }

        // Displays a video related to the neglected life area
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
                Log.i(TAG, "learning case");
                videoId = getString(R.string.video_learning);
                break;
            case "finances":
                videoId = getString(R.string.video_finances);
                break;
            default:
                Log.i(TAG, "default case");
                videoId = getString(R.string.video_default);
                videoIsDefault = true;

                // Hides video title if the default video is shown
                videoTitleTextView.setVisibility(GONE);
        }

        // YouTube video player tutorial found at https://www.youtube.com/watch?v=yyduqrCpKGg
        youTubePlayerView = findViewById(R.id.youtubePlayer);

        // Hides the toolbar and video title when fullscreen is entered
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                videoIsFullscreen = true;
                getSupportActionBar().hide();
                videoTitleTextView.setVisibility(GONE);
            }

            // Shows the toolbar and video title when fullscreen is exited
            @Override
            public void onYouTubePlayerExitFullScreen() {
                if (getResources().getConfiguration().orientation == 1) {
                    videoIsFullscreen = false;
                    getSupportActionBar().show();
                }
            }
        });

        // Cues the YouTube video
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }

    /**
     * Hides toolbar and video title if orientation changes to landscape
     * Shows them if orientation changes to portrait
     *
     * @param newConfig device configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Hides toolbar and video title if orientation changes to landscape
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            videoTitleTextView.setVisibility(GONE);

            // Shows toolbar if orientation changes to portrait, unless video is fullscreen
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && !videoIsFullscreen) {
            getSupportActionBar().show();

            // Shows video title unless video is default
            if (!videoIsDefault) {
                videoTitleTextView.setVisibility(VISIBLE);
            }
        }
    }
}