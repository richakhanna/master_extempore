package com.richdroid.masterextempore.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.model.AttemptedTopic;
import com.richdroid.masterextempore.ui.adapter.AttemptedListAdpater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class VideoListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private AttemptedListAdpater mAdapter;
    private List<AttemptedTopic> mDataSetList = null;

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

    public VideoListFragment() {
        mDataSetList = new ArrayList<>();
        mDataSetList.add(new AttemptedTopic("hello"));
        mDataSetList.add(new AttemptedTopic("Bello"));
        mDataSetList.add(new AttemptedTopic("Kello"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_videos, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.videolistview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new AttemptedListAdpater(getActivity(), mDataSetList);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

}
