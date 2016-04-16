package com.richdroid.masterextempore.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.app.AppController;
import com.richdroid.masterextempore.model.Topic;
import com.richdroid.masterextempore.model.TopicLists;
import com.richdroid.masterextempore.network.DataManager;
import com.richdroid.masterextempore.network.DataRequester;
import com.richdroid.masterextempore.ui.adapter.TryListAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PlaceholderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
  /**
   * The fragment argument representing the section number for this
   * fragment.
   */
  private static final String ARG_SECTION_NUMBER = "section_number";
  private static final String TAG = PlaceholderFragment.class.getSimpleName();
  private RecyclerView mRecyclerView;
  private LinearLayoutManager mLayoutManager;
  private TryListAdapter mAdapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private DataManager mDataMan;
  private ArrayList<Topic> mTopicList = null;

  /**
   * Returns a new instance of this fragment for the given section
   * number.
   */
  public static PlaceholderFragment newInstance(int sectionNumber) {
    PlaceholderFragment fragment = new PlaceholderFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public PlaceholderFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_all_topics, container, false);
    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.alltopics);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
    swipeRefreshLayout.setOnRefreshListener(this);
    mTopicList = new ArrayList<>();
    mAdapter = new TryListAdapter(getActivity(), mTopicList);
    mRecyclerView.setAdapter(mAdapter);


    return rootView;
  }

  @Override public void onResume() {
    super.onResume();
    swipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
        swipeRefreshLayout.setRefreshing(true);

        fetchTopicsLists();
      }
    });
  }

  private void fetchTopicsLists() {
    AppController app = ((AppController) getActivity().getApplication());
    mDataMan = app.getDataManager();
    mDataMan.getAllTopicLists(new WeakReference<DataRequester>(mRequestAllContacts), TAG);
  }

  DataRequester mRequestAllContacts = new DataRequester() {
    @Override public void onFailure(Throwable error) {
      //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
      swipeRefreshLayout.setRefreshing(false);
    }

    @Override public void onSuccess(Object respObj) {
      TopicLists topicLists = (TopicLists) respObj;
      mTopicList.clear();

      for (Topic topic : topicLists.getAllTopic()) {
        mTopicList.add(topic);
      }
      mAdapter.notifyDataSetChanged();
      swipeRefreshLayout.setRefreshing(false);
    }
  };

  @Override public void onRefresh() {
    fetchTopicsLists();
  }

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    Log.d(TAG, "Called again");
    if(getActivity() == null){
      return;
    }
    if(isVisibleToUser) {
      fetchTopicsLists();
    }
  }
}