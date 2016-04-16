package com.richdroid.masterextempore.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.ui.adapter.TryListAdapter;
import com.richdroid.masterextempore.utils.CalendarUtil;
import com.richdroid.masterextempore.utils.FileUtil;

import java.io.File;

public class VideoRecordingActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView mVideoView;
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();
    private ImageView mPlayButton;
    private String topicCategory;
    private String topicId;
    private Uri videoFileUri;
    private boolean audioAccepted;
    private boolean writeExternalStorageAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recording);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            topicCategory = extras.getString(TryListAdapter.CATEGORY);
            topicId = extras.getString(TryListAdapter.TOPIC_ID);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        // Setup a play button to start the video
        mPlayButton = (ImageView) findViewById(R.id.play_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

        int permsRequestCode = 200;

        if (!hasPermission("android.permission.RECORD_AUDIO") || !hasPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            requestPermissions(perms, permsRequestCode);
        } else {
            //You have been granted both permission
            dispatchTakeVideoIntent();
        }


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

        String videoNamePrefix = topicCategory + "_" + topicId;
        String albumName = "Lideo";

        // To generate unique image name for the video with a prefix;
        String videoFileName = CalendarUtil.generateUniqueImageName(videoNamePrefix);

        // To get a file instance for a videoName in a particular album
        // creating this file to save the video
        File videoFile = FileUtil.getOutputMediaFile(this, albumName, videoFileName);

        if (videoFile == null) {
            Log.d("In PictureCallback", "Error creating media file, check storage permissions");
            return;
        }

        videoFileUri = Uri.fromFile(videoFile);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);

        // set the video image quality to high
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

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

    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                audioAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                writeExternalStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (audioAccepted && writeExternalStorageAccepted) {
                    //You have been granted both permission
                    dispatchTakeVideoIntent();
                }

                break;
        }
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

}
