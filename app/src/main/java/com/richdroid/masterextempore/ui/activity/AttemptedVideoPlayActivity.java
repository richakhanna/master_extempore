package com.richdroid.masterextempore.ui.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.richdroid.masterextempore.R;

import java.io.File;
import java.util.ArrayList;

public class AttemptedVideoPlayActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private ImageView mPlayButton;
    private Uri videoFileUri;
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempted_video_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        // Setup a play button to start the video
        mPlayButton = (ImageView) findViewById(R.id.play_button);


        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            ArrayList<String> fileNameList = extras.getStringArrayList("video_path");
            for (String str : fileNameList) {
                videoFileUri = Uri.fromFile(new File(str));
                Log.d("AttemptedVideoPlay", "videoFileUri : " + videoFileUri);
            }
        }

        mVideoView.setVideoURI(videoFileUri);
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.start();
        // hide button once playback starts
        mPlayButton.setVisibility(View.GONE);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!bVideoIsBeingTouched) {
                    bVideoIsBeingTouched = true;
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                        // show button once playback pauses
                        mPlayButton.setVisibility(View.VISIBLE);
                    } else {
                        mVideoView.start();
                        // hide button once playback starts
                        mPlayButton.setVisibility(View.GONE);
                    }
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            bVideoIsBeingTouched = false;
                        }
                    }, 100);
                }
                return true;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer vmp) {
                // show button once playback completes
                mPlayButton.setVisibility(View.VISIBLE);
            }


        });

//        mPlayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mVideoView.setVideoURI(videoFileUri);
//                mVideoView.setVisibility(View.VISIBLE);
//                mVideoView.start();
//                // hide button once playback starts
//                mPlayButton.setVisibility(View.GONE);
//            }
//        });


    }

}
