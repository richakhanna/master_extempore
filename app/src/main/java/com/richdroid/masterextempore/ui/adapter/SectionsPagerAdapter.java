package com.richdroid.masterextempore.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.richdroid.masterextempore.ui.fragment.CategoryFragement;
import com.richdroid.masterextempore.ui.fragment.PlaceholderFragment;
import com.richdroid.masterextempore.ui.fragment.VideoListFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CategoryFragement.newInstance(position);
            case 1:
                return PlaceholderFragment.newInstance(position);
            case 2:
                return VideoListFragment.newInstance(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }
}