package com.richdroid.masterextempore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Intent intent = new Intent(this, VideoRecordingActivity.class);
        startActivity(intent);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
