package com.richdroid.masterextempore.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.model.ContactDetail;

import java.util.List;

/**
 * Created by richa.khanna on 3/18/16.
 */
public class ContactDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static Context mContext;
    private static List<ContactDetail> mDatasetList;
    private static final int PERSON_CONTACT_DETAIL = 0;
    private static final int ANIMAL_CONTACT_DETAIL = 1;
    // Allows to remember the last item shown on screen
    private int lastAnimatedItemPosition = -1;


    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactDetailAdapter(Context context, List<ContactDetail> datasetList) {
        mContext = context;
        mDatasetList = datasetList;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ContactDetailViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTVName;
        private TextView mTVEmail;
        private TextView mTVLocation;
        private TextView mTVType;
        private LinearLayout mContainer;
        private ImageButton mDeleteButton;


        public ContactDetailViewHolder(View view) {
            super(view);
            this.mTVName = (TextView) view.findViewById(R.id.tv_name);
            this.mTVEmail = (TextView) view.findViewById(R.id.tv_email);
            this.mTVLocation = (TextView) view.findViewById(R.id.tv_location);
            this.mTVType = (TextView) view.findViewById(R.id.tv_type);
            this.mContainer = (LinearLayout) view.findViewById(R.id.contact_layout_container);
            this.mDeleteButton = (ImageButton) view.findViewById(R.id.button_delete);
            this.mDeleteButton.setOnClickListener(this);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            ContactDetail contactDetail = mDatasetList.get(itemPosition);

            switch (view.getId()) {
                case R.id.button_delete:
                    mDatasetList.remove(contactDetail);
                    notifyItemRemoved(itemPosition);
                    break;

                default:
                    Toast.makeText(mContext, "You clicked at position " + itemPosition +
                            " on contact details of : " +
                            contactDetail.getName(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int itemPosition = getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                ContactDetail contactDetail = mDatasetList.get(itemPosition);

                //If delete button is already shown, hide it by setting flag to false
                //And the item will be updated when onBindViewHolder will be called.
                if (contactDetail.isToShowDeleteIcon()) {
                    contactDetail.setToShowDeleteIcon(false);
                } else {
                    contactDetail.setToShowDeleteIcon(true);
                }

                notifyItemChanged(itemPosition);
            }
            return true;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ContactDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_detail_view, parent, false);

        switch (viewType) {
            case PERSON_CONTACT_DETAIL:
            case ANIMAL_CONTACT_DETAIL:
                return new ContactDetailViewHolder(view);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ContactDetailViewHolder cusHolder = (ContactDetailViewHolder) holder;
        cusHolder.mTVName.setText(mDatasetList.get(position).getName());
        cusHolder.mTVEmail.setText(mDatasetList.get(position).getEmail());
        cusHolder.mTVLocation.setText(mDatasetList.get(position).getLocation());


        switch (getItemViewType(position)) {
            case PERSON_CONTACT_DETAIL:
                cusHolder.mTVType.setText("Person");
                break;
            case ANIMAL_CONTACT_DETAIL:
                cusHolder.mTVType.setText("Animal");
                break;
        }

        if (mDatasetList.get(position).isToShowDeleteIcon()) {
            cusHolder.mDeleteButton.setVisibility(View.VISIBLE);
        } else {
            cusHolder.mDeleteButton.setVisibility(View.GONE);
        }

        setEnterAnimation(cusHolder.mContainer, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ContactDetail contactDetail = mDatasetList.get(position);
        String contactType = contactDetail.getType();
        if (contactType.equalsIgnoreCase("Animal")) {
            return ANIMAL_CONTACT_DETAIL;
        }
        return PERSON_CONTACT_DETAIL;
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

    /**
     * The view could be reused while the animation is been happening.
     * In order to avoid that is recommendable to clear the animation when is detached.
     */
    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((ContactDetailViewHolder) holder).mContainer.clearAnimation();
    }
}
