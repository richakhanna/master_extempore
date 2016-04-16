package com.richdroid.masterextempore.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.ui.adapter.CategoryGridAdapter;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class CategoryFragement extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private GridLayoutManager glm;
    private CategoryGridAdapter mAdapter;
    private Activity mContext;

    public static CategoryFragement newInstance(int sectionNumber) {
        CategoryFragement fragment = new CategoryFragement();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_videos, container, false);
        mContext = getActivity();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.videolistview);
        //mRecyclerView.setHasFixedSize(true);
        glm = new GridLayoutManager(mContext, 3);
        glm.setOrientation(GridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(glm);
        // specify an adapter
        mAdapter = new CategoryGridAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
