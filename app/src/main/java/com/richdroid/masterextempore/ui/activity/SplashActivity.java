package com.richdroid.masterextempore.ui.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.richdroid.masterextempore.R;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class SplashActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final long SPLASH_TIME = 3000;
    private static final int RC_SIGN_IN = 2;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private ImageView image;
    private Runnable mRunnable;
    private Handler mHandler;
    private GoogleApiClient mGoogleApiClient;
    private AccountManager mAccountManager;
    //AIzaSyB8IO0yqYVzF2kn9Elqt-Xi9Hv7c8OVb7Q

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_splash);
        image = (ImageView) findViewById(R.id.splash_image);
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        if (accounts.length > 0) {
            start();
        }
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(image, "scaleX", 1.0f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(image, "scaleY", 1.0f, 1.2f);

        scaleY.setDuration(10000);
        scaleY.start();
        scaleX.setDuration(10000);
        scaleX.start();

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mRunnable = new Runnable() {

            public void run() {
                start();
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, SPLASH_TIME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void start() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            String email = result.getSignInAccount().getEmail();
            Log.d(TAG, "Google:" + "Login " + email);
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();

            start();
        } else {
            // Signed out, show unauthenticated UI.
            Log.d(TAG, "Google:" + "Logout ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.skip:
                start();
                break;
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                // ...
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
