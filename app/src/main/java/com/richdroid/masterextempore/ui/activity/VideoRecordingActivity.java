package com.richdroid.masterextempore.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.app.AppController;
import com.richdroid.masterextempore.network.DataManager;
import com.richdroid.masterextempore.network.MultipartRequest;
import com.richdroid.masterextempore.ui.adapter.TryListAdapter;
import com.richdroid.masterextempore.utils.CalendarUtil;
import com.richdroid.masterextempore.utils.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class VideoRecordingActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private VideoView mVideoView;
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();
    private ImageView mPlayButton;
    private String topicCategory;
    private String topicId;
    private Uri videoFileUri;
    private boolean audioAccepted;
    private boolean writeExternalStorageAccepted;
    private String videoFileName;
    private byte[] multipartBody;
    private DataManager mDataMan;


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
        videoFileName = CalendarUtil.generateUniqueImageName(videoNamePrefix);

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
            if(intent ==null || intent.getData() == null){
                return;
            }
            Uri videoUri = intent.getData();
            mVideoView.setVideoURI(videoUri);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.start();
            // hide button once playback starts
            mPlayButton.setVisibility(View.GONE);

            byte[] videoFileData = null;
            try {
                videoFileData = readBytes(videoUri);
            } catch (IOException e) {
                Log.e("VideoRecordingActivity", "IO exception : " + e.getMessage());
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            if (videoFileData != null) {
                try {
                    // the first file
                    buildPart(dos, videoFileData, videoFileName);
                    // send multipart form data necessary after file data
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // pass to multipart body
                    multipartBody = bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final String BASE_URL = "http://172.20.172.49:8989";
                // /users/{userId}/topics/{topicId}/videos?filename=testVideoName
                String userId = "5711e13ed4c6f0df5adf8a17";
                String paramsUrl = BASE_URL + "/users/" + userId + "/topics/" + topicId + "/videos?filename=" + videoFileName;
                MultipartRequest multipartRequest = new MultipartRequest(paramsUrl, null, mimeType, multipartBody, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Toast.makeText(VideoRecordingActivity.this, "Upload successfully!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VideoRecordingActivity.this, "Upload failed!\r\n" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                AppController app = ((AppController) getApplication());
                mDataMan = app.getDataManager();
                mDataMan.addToRequestQueueInDM(multipartRequest, "VideoRecordingActivity");

            }


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

    private byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] readBytes(Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"video\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

}
