package com.richdroid.masterextempore.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.richdroid.masterextempore.R;
import com.richdroid.masterextempore.app.AppController;
import com.richdroid.masterextempore.model.ContactDetail;
import com.richdroid.masterextempore.network.DataManager;
import com.richdroid.masterextempore.ui.adapter.SectionsPagerAdapter;
import com.richdroid.masterextempore.utils.ProgressBarUtil;
import com.richdroid.masterextempore.utils.Utilities;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

  private static final String TAG = MainActivity.class.getSimpleName();
  private static final int NUM_PAGES = 3;
  private List<ContactDetail> mDatasetList;
  private DataManager mDataMan;
  private ProgressBarUtil mProgressBar;
  private SectionsPagerAdapter mSectionsPagerAdapter;
  private ViewPager mViewPager;
  private TabLayout mTabLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //setSupportActionBar(toolbar);

    AppController app = ((AppController) getApplication());
    mDataMan = app.getDataManager();
    mTabLayout = (TabLayout) findViewById(R.id.tabs);
    mViewPager = (ViewPager) findViewById(R.id.container);

    for (int pos = 0; pos < NUM_PAGES; pos++) {
      mTabLayout.addTab(mTabLayout.newTab().setText(Utilities.getPageTitle(this, pos)));
    }
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    mViewPager.setAdapter(mSectionsPagerAdapter);

    mTabLayout.setOnTabSelectedListener(this);
  }

  @Override public void onTabSelected(TabLayout.Tab tab) {
    mViewPager.setCurrentItem(tab.getPosition());
    mSectionsPagerAdapter.notifyDataSetChanged();
  }

  @Override public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override public void onTabReselected(TabLayout.Tab tab) {

  }
}
