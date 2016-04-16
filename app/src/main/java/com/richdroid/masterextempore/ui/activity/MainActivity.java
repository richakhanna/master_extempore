package com.richdroid.masterextempore.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.app.AppController;
import com.richdroid.masterextempore.model.AllContactResponse;
import com.richdroid.masterextempore.model.ContactDetail;
import com.richdroid.masterextempore.network.DataManager;
import com.richdroid.masterextempore.network.DataRequester;
import com.richdroid.masterextempore.ui.adapter.ContactDetailAdapter;
import com.richdroid.masterextempore.utils.ProgressBarUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ContactDetail> mDatasetList;
    private DataManager mDataMan;
    private ProgressBarUtil mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppController app = ((AppController) getApplication());
        mDataMan = app.getDataManager();

        mProgressBar = new ProgressBarUtil(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDatasetList = new ArrayList<ContactDetail>();

        Log.v(TAG, "Calling : get Contact Detail Api");
        mProgressBar.show();
        mDataMan.getAllContacts(
                new WeakReference<DataRequester>(mContactDetailRequester), TAG);

        // specify an adapter
        mAdapter = new ContactDetailAdapter(this, mDatasetList);
        mRecyclerView.setAdapter(mAdapter);

    }

    private DataRequester mContactDetailRequester = new DataRequester() {

        @Override
        public void onFailure(Throwable error) {
            if (isFinishing()) {
                return;
            }

            mProgressBar.hide();
            Log.v(TAG, "Failure : contact detail onFailure");
        }

        @Override
        public void onSuccess(Object respObj) {
            if (isFinishing()) {
                return;
            }

            mProgressBar.hide();
            Log.v(TAG, "Success : Contact detail Data : " + new Gson().toJson(respObj).toString());
            AllContactResponse response = (AllContactResponse) respObj;

            if (response != null && response.getAllContactDetails() != null && response.getAllContactDetails().size() > 0) {
                List<ContactDetail> contactDetailList = response.getAllContactDetails();
                for (ContactDetail contact : contactDetailList) {
                    mDatasetList.add(contact);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

}
