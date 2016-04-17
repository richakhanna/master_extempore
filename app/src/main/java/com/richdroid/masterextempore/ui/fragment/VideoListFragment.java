package com.richdroid.masterextempore.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.app.AppController;
import com.richdroid.masterextempore.model.Topic;
import com.richdroid.masterextempore.model.TopicLists;
import com.richdroid.masterextempore.network.DataManager;
import com.richdroid.masterextempore.network.DataRequester;
import com.richdroid.masterextempore.ui.adapter.TryListAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class VideoListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = VideoListFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TryListAdapter mAdapter;
    private DataManager mDataMan;
    private ArrayList<Topic> mTopicList;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static VideoListFragment newInstance(int sectionNumber) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_videos, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.videolistview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTopicList = new ArrayList<>();
        // specify an adapter
        mAdapter = new TryListAdapter(getActivity(), mTopicList);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchAttemptedTopicsList();
    }


    private void fetchAttemptedTopicsList() {
        AppController app = ((AppController) getActivity().getApplication());
        mDataMan = app.getDataManager();
        mDataMan.getAttemptedTopicList(new WeakReference<DataRequester>(mRequestAttemptedTopics), TAG);
    }

    DataRequester mRequestAttemptedTopics = new DataRequester() {
        @Override
        public void onFailure(Throwable error) {
            if (!isAdded()) {
                return;
            }
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(Object respObj) {
            if (!isAdded()) {
                return;
            }
            TopicLists topicLists = (TopicLists) respObj;
            mTopicList.clear();

            for (Topic topic : topicLists.getAllTopic()) {
                mTopicList.add(topic);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

}
