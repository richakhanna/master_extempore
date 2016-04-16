package com.richdroid.masterextempore.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.richdroid.masterextempore.R;

public class VideoRecordingActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView mVideoView;
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();
    private ImageView mPlayButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recording);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        // Setup a play button to start the video
        mPlayButton = (ImageView) findViewById(R.id.play_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dispatchTakeVideoIntent();


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
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            mVideoView.setVideoURI(videoUri);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.start();
            // hide button once playback starts
            mPlayButton.setVisibility(View.GONE);
        }
    }

}
