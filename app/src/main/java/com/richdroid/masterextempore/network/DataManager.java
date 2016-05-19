package com.richdroid.masterextempore.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.model.TopicLists;
import com.richdroid.masterextempore.utils.Utilities;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by richa.khanna on 3/23/16.
 */
public class DataManager {

    private static final String TAG = DataManager.class.getSimpleName();
    public static final String BASE_URL = "http://10.20.103.17:8989";
    private static DataManager mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private SharedPreferences mPref;

    private DataManager(Context context) {
        mContext = context;
    }

    public static synchronized DataManager getInstance(Context context) {
        if (mInstance == null) {
            Log.v(TAG, "Creating data manager instance");
            mInstance = new DataManager(context.getApplicationContext());
        }
        return mInstance;
    }

    public void init() {
        mRequestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    /**
     * Add the request with tag to volley request queue
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        // set the default tag if tag is empty
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        mRequestQueue.add(request);
    }

    /**
     * Cancel any pending volley request associated with the {param requestTag}
     */
    public void cancelPendingRequests(String requestTag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(requestTag);
        }
    }

    /**
     * Cleanup & save anything that needs saving as app is going away.
     */
    public void terminate() {
        mRequestQueue.stop();
    }

    public void getAllTopicLists(final WeakReference<DataRequester> wRequester, String tag) {
        Log.v(TAG, "Api call : get topic lists");
        JSONObject obj = new JSONObject();
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        int length = Utilities.categoryList.length;
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (pref.getBoolean(Utilities.categoryList[i], false)) {
                arrayList.add(Utilities.categoryList[i]);
            }
        }

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.v(TAG, "Success : get topic returned a response");
                DataRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }
                TopicLists allContactResponse = null;
                if (jsonObject != null && !TextUtils.isEmpty(jsonObject.toString())) {
                    Log.v(TAG, "Success : converting Json to Java Object via Gson");
                    allContactResponse = new Gson().fromJson(jsonObject.toString(), TopicLists.class);
                }

                if (req != null) {
                    if (allContactResponse != null) {
                        req.onSuccess(allContactResponse);
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DataRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }
                if (req != null) {
                    req.onFailure(volleyError);
                }
            }
        };


        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
        builder.appendPath("topics").appendQueryParameter("level", "Easy");
        for (String str : arrayList) {
            builder.appendQueryParameter("categories", str);
        }

        String paramurl = builder.build().toString();
        CustomJsonObjectRequest request =
                new CustomJsonObjectRequest(Request.Method.GET, paramurl, obj, responseListener,
                        errorListener);
        addToRequestQueue(request, tag);
    }

    public void addToRequestQueueInDM(Request request, String tag) {
        addToRequestQueue(request, tag);
    }

    public void postUserData(final WeakReference<DataRequester> wRequester, JSONObject obj, String tag) {
        Log.v(TAG, "Api call : post user data");
        final SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.v(TAG, "Success : post user data returned a response");
                SharedPreferences.Editor editor = pref.edit();

                try {
                    String userId = jsonObject.getString("userId");
                    Log.v(TAG, "Success : obtained userId : " + userId);
                    editor.putString(mContext.getString(R.string.user_id_key), jsonObject.getString("userId"));
                    editor.commit();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                DataRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.v(TAG, "Error : post user data failed");
                DataRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }
                if (req != null) {
                    req.onFailure(volleyError);
                }
            }
        };

        CustomJsonObjectRequest request =
                new CustomJsonObjectRequest(Request.Method.POST, BASE_URL + "/users", obj, responseListener,
                        errorListener);
        addToRequestQueue(request, tag);
    }


    public void getAttemptedTopicList(final WeakReference<DataRequester> wRequester, String tag) {
        Log.v(TAG, "Api call : get attempted topic list");
        JSONObject obj = new JSONObject();

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.v(TAG, "Success : get attempted topic list returned a response");
                DataRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }
                TopicLists allContactResponse = null;
                if (jsonObject != null && !TextUtils.isEmpty(jsonObject.toString())) {
                    Log.v(TAG, "Success : converting Json to Java Object via Gson");
                    allContactResponse = new Gson().fromJson(jsonObject.toString(), TopicLists.class);
                }

                if (req != null) {
                    if (allContactResponse != null) {
                        req.onSuccess(allContactResponse);
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.v(TAG, "Error : get attempted topic list failed");
                DataRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }
                if (req != null) {
                    req.onFailure(volleyError);
                }
            }
        };

        // http://172.20.172.49:8989/users/5711e13ed4c6f0df5adf8a17/attempted_topics
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userId = mPref.getString(mContext.getString(R.string.user_id_key),
                mContext.getString(R.string.user_id_default_value));
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
        builder.appendPath("users").appendPath(userId).appendPath("attempted_topics");

        String url = builder.build().toString();
        CustomJsonObjectRequest request =
                new CustomJsonObjectRequest(Request.Method.GET, url, obj, responseListener,
                        errorListener);
        addToRequestQueue(request, tag);
    }

}
