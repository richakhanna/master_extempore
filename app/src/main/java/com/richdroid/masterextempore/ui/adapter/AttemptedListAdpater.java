package com.richdroid.masterextempore.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.model.Topic;

import java.util.List;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class AttemptedListAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Topic> mDataSetList;
    private final Context mContext;
    private int lastAnimatedItemPosition = -1;

    public class ListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final TextView mTopicName;
        private LinearLayout mContainer;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.mTopicName = (TextView) itemView.findViewById(R.id.tv_topic);
            this.mContainer = (LinearLayout) itemView.findViewById(R.id.container);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public AttemptedListAdpater(Context context, List<Topic> datasetList) {
        mContext = context;
        mDataSetList = datasetList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attemted_topic_list_item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ListViewHolder viewHolder = (ListViewHolder) holder;
        viewHolder.mTopicName.setText(mDataSetList.get(position).getTopicName());

        setEnterAnimation(viewHolder.mContainer, position);
    }

    private void setEnterAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it will be animated
        if (position > lastAnimatedItemPosition) {
            //Animation using xml
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.translate_left);
            viewToAnimate.startAnimation(animation);

            //            Or Animation using ObjectAnimator
            //            ObjectAnimator anim = ObjectAnimator.ofFloat(viewToAnimate, "translationX", 300, 0);
            //            anim.setDuration(1500);
            //            DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(2);
            //            anim.setInterpolator(decelerateInterpolator);
            //            anim.start();

            //            Or Animation using setTranslationX
            //            viewToAnimate.setTranslationX(300);
            //            viewToAnimate.animate().translationX(0).
            //                    setInterpolator(new DecelerateInterpolator(2)).setDuration(1500).start();

            lastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSetList.size();
    }
}
