package com.richdroid.masterextempore.ui.activity;

import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.richdroid.masterextempore.app.AppController;
import com.richdroid.masterextempore.network.DataRequester;
import com.richdroid.masterextempore.utils.Utilities;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class SplashActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long SPLASH_TIME = 2000;
    private static final int RC_SIGN_IN = 2;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final String LOGIN = "login";
    private ImageView image;
    private Runnable mRunnable;
    private Handler mHandler;
    private GoogleApiClient mGoogleApiClient;
    private AccountManager mAccountManager;
    private AppController mDataMan;
    //AIzaSyB8IO0yqYVzF2kn9Elqt-Xi9Hv7c8OVb7Q
    private SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(mPref.getBoolean(LOGIN,false)){
            start();
        }
        setContentView(R.layout.acitivity_splash);
        image = (ImageView) findViewById(R.id.splash_image);
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
        mGoogleApiClient.registerConnectionCallbacks(this);
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.skip).setOnClickListener(this);
//        mRunnable = new Runnable() {
//
//            public void run() {
//                //  start();
//            }
//        };
//        mHandler = new Handler();
//        mHandler.postDelayed(mRunnable, SPLASH_TIME);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            String email = result.getSignInAccount().getEmail();
            String userName = result.getSignInAccount().getDisplayName();
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", userName);
                obj.put("email", email);
            }catch (Exception e){

            }
            mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean(LOGIN,true);
            editor.commit();
            mDataMan = ((AppController)getApplication());
            mDataMan.getDataManager().postUserData(new WeakReference<DataRequester>(new DataRequester() {
                @Override
                public void onFailure(Throwable error) {
                    Log.d(TAG, "Data:" + "Failure" );
                }

                @Override
                public void onSuccess(Object respObj) {
                    Log.d(TAG, "Data:" + "Success" );
                }
            }),obj,TAG);

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
                if(Utilities.isNetworkAvailable(getApplicationContext())) {
                    signIn();
                }else {
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
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
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "On Conn failed");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Onconnectd");
        if(bundle !=null) {
            findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
            mRunnable = new Runnable() {
                public void run() {
                    start();
                }
            };
            mHandler = new Handler();
            mHandler.postDelayed(mRunnable, SPLASH_TIME);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "conectionn suspended ");
    }
}
