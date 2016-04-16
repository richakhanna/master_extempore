package com.richdroid.masterextempore.app;

import android.app.Application;
import android.util.Log;

import com.richdroid.masterextempore.network.DataManager;


/**
 * Created by richa.khanna on 3/20/16.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    // Different Managers
    private DataManager mDataMan;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "App started");
        initApp();
    }

    private void initApp() {
        mDataMan = DataManager.getInstance(AppController.this);
        mDataMan.init();
    }

    /**
     * Get the data manager instance
     */
    public synchronized DataManager getDataManager() {
        return mDataMan;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mDataMan != null) {
            mDataMan.terminate();
        }
    }
}
