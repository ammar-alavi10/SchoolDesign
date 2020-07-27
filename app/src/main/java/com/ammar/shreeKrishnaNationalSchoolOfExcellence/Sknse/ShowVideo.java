package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.core.content.ContextCompat;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.YoutubeApiKey;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ShowVideo extends YouTubeBaseActivity {

    private Application application;
    private String name, type, url;
    SimpleExoPlayer exoPlayer;
    YouTubePlayer youTubePlayer;
    PlayerView playerView;
    YouTubePlayerView youTubePlayerView;
    boolean fullScreen;
    TextView titleTv;
    private ImageView fullscreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videoview);

        name = getIntent().getStringExtra("videotitle");
        type = getIntent().getStringExtra("videotype");
        url = getIntent().getStringExtra("videourl");

        application = getApplication();
        titleTv = findViewById(R.id.video_name_tv);
        titleTv.setText(name);
        playerView = findViewById(R.id.exoplayer);
        youTubePlayerView = findViewById(R.id.youtube_player);

        if(type.equals("firebase"))
        {
            setExoplayer();
        }
        else
        {
            setYoutubePlayer();
        }
    }

    @Override
    public void onBackPressed() {
        if (fullScreen){
            youTubePlayer.setFullscreen(false);
        } else{
            super.onBackPressed();
        }
    }

    private void setYoutubePlayer() {
        youTubePlayerView.setVisibility(View.VISIBLE);
        YouTubePlayer.OnInitializedListener mListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if(!wasRestored)
                {
                    Log.d("YoutubePlayer", "Successfully initialized");
                    youTubePlayer = player;
                    youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

                        @Override
                        public void onFullscreen(boolean _isFullScreen) {
                            fullScreen = _isFullScreen;
                        }
                    });
                    String videoId = extractYTId(url);
                    youTubePlayer.loadVideo(videoId);
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("YoutubePlayer", "Can't be initialized" + youTubeInitializationResult.toString());
            }
        };

        youTubePlayerView.initialize(YoutubeApiKey.getKey(), mListener);
    }

    private void setExoplayer() {
        try {
            playerView.setVisibility(View.VISIBLE);
            initializeFullScreen();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application);
            Uri videoUri = Uri.parse(url);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        } catch (Exception e)
        {
            Log.d("VideoPlayer", e.getStackTrace().toString());
        }
    }

    private void initializeFullScreen() {
        final boolean[] fullscreen = {false};
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen[0]) {
                    titleTv.setVisibility(View.VISIBLE);
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(ShowVideo.this, R.drawable.ic_fullscreen_open));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen[0] = false;
                }else{
                    titleTv.setVisibility(View.GONE);
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(ShowVideo.this, R.drawable.ic_fullscreen_close));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen[0] = true;
                }
            }
        });
    }

    private String extractYTId(String videoUrl) {
        final String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        if (videoUrl == null || videoUrl.trim().length() <= 0){
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}