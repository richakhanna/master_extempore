package com.richdroid.masterextempore.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.model.Topic;
import com.richdroid.masterextempore.ui.activity.AttemptedVideoPlayActivity;
import com.richdroid.masterextempore.utils.FileUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Created by richa.khanna on 16/04/16.
 */
public class AttemptedListAdapter extends RecyclerView.Adapter<AttemptedListAdapter.ListViewHolder> {

    public static final String CATEGORY = "category";
    public static final String TOPIC_ID = "topic_id";
    private final Context mContext;
    private SharedPreferences mPref;
    private ArrayList<Topic> mTopicList = null;


    public class ListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final TextView mTopicName;
        private TextView mDetailText;
        private LinearLayout mContainer;
        private LinearLayout mTextLayout;


        public ListViewHolder(View itemView) {
            super(itemView);
            this.mTopicName = (TextView) itemView.findViewById(R.id.title_text_view);
            this.mDetailText = (TextView) itemView.findViewById(R.id.details_text_view);
            this.mContainer = (LinearLayout) itemView.findViewById(R.id.linear_links);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final String topicId = mTopicList.get(position).getId();

            File dir = FileUtil.getOutputMediaFolder(mContext, "Lideo");
            File[] files = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.contains(topicId);
                }
            });


            ArrayList<String> fileNameList = new ArrayList<>();
            for (File file : files) {
                Log.d("AttemptedListAdapter", "file uri : " + Uri.fromFile(file));
                Log.d("AttemptedListAdapter", "absolute path : " + file.getAbsolutePath());
                fileNameList.add(file.getAbsolutePath());
            }

            if (files.length > 0) {
                Intent intent = new Intent(mContext, AttemptedVideoPlayActivity.class);
                intent.putStringArrayListExtra("video_path", fileNameList);
                mContext.startActivity(intent);
            }


        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public AttemptedListAdapter(Context context, ArrayList<Topic> topicList) {
        mContext = context;
        mTopicList = topicList;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    }

    @Override
    public AttemptedListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.try_list_item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.mTopicName.setText(mTopicList.get(position).getTopicName());
        if (mTopicList.get(position).getLinks() != null
                && mTopicList.get(position).getLinks().size() > 0) {
            viewHolder.mDetailText.setText(mTopicList.get(position).getLinks().get(0));
        } else {
            viewHolder.mContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mTopicList == null || mTopicList.size() == 0) {
            return mTopicList.size();
        } else {
            return mTopicList.size();
        }
    }
}
