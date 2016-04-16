package com.richdroid.masterextempore.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.model.Topic;
import java.util.ArrayList;

public class TryListAdapter extends RecyclerView.Adapter<TryListAdapter.ListViewHolder> {

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
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override public void onClick(View v) {
      //int position = getAdapterPosition();
      //SharedPreferences.Editor editor = mPref.edit();
      //if(mPref.getBoolean(categoryList[position], false)){
      //  this.mTextLayout.setBackgroundColor(mContext.getResources().getColor(R.color.blacktranslucent));
      //  editor.putBoolean(categoryList[position], false);
      //}else{
      //  this.mTextLayout.setBackgroundColor(
      //      mContext.getResources().getColor(R.color.greentranslucent));
      //  editor.putBoolean(categoryList[position], true);
      //}
      //editor.commit();
    }

    @Override public boolean onLongClick(View v) {
      return false;
    }
  }

  public TryListAdapter(Context context, ArrayList<Topic> topicList) {
    mContext = context;
    mTopicList = topicList;
    mPref = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
  }

  @Override
  public TryListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.try_list_item, parent, false);

    return new ListViewHolder(view);
  }

  @Override public void onBindViewHolder(ListViewHolder viewHolder, int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    viewHolder.mTopicName.setText(mTopicList.get(position).getTopicName());
  }

  @Override public int getItemCount() {
    return mTopicList.size();
  }
}
