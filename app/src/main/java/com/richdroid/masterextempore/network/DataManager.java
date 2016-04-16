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
  private static DataManager mInstance;
  private Context mContext;
  private RequestQueue mRequestQueue;

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
      @Override public void onResponse(JSONObject jsonObject) {
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
      @Override public void onErrorResponse(VolleyError volleyError) {
        DataRequester req = null;
        if (wRequester != null) {
          req = wRequester.get();
        }
        if (req != null) {
          req.onFailure(volleyError);
        }
      }
    };

    String url = "http://172.20.172.49:8989";
    Uri.Builder builder = Uri.parse(url).buildUpon();
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
}
