package com.example.uilayer.milestones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.MilesAdapter;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Milestones;
import com.example.uilayer.milestones.betterAdapter.model.Training;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArchiveActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_archive, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String IS_MILE = "is_mile";
        @BindView(R.id.recycler_fragment_archive)
        RecyclerView archiveRecycler;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(boolean isMile) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_MILE, isMile);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_archive, container, false);
            ButterKnife.bind(this, rootView);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            archiveRecycler.setLayoutManager(mLayoutManager);
            loadAdapterItems();
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(archiveRecycler.getContext(),
                    mLayoutManager.getOrientation());

            archiveRecycler.addItemDecoration(mDividerItemDecoration);
            archiveRecycler.setAdapter(milestonesAdapter);

 /*           if (getArguments().getBoolean(IS_MILE))
                archiveRecycler.setAdapter(null);
            else
                archiveRecycler.setAdapter(null);*/

            return rootView;
        }
        MilesAdapter milestonesAdapter;
        void loadAdapterItems() {
            ArrayList<Milestones> list = new ArrayList<>();
            list.add(new Mile(1, 1, "Mile", "Mile one"));
            list.add(new Mile(1, 2, "Mile", "Mile two"));
            list.add(new Training(1, 2, "Training", "Training one"));
            list.add(new Training(1, 2, "Training", "Training two"));
            list.add(new Mile(1, 3, "Mile", "Mile three"));
            list.add(new Training(1, 2, "Training", "Training three"));
            list.add(new Mile(1, 4, "Mile", "Mile four"));
            list.add(new Mile(1, 5, "Mile", "Mile five"));
            milestonesAdapter = new MilesAdapter(getActivity(), list);
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(true);
            }
            return PlaceholderFragment.newInstance(false);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MILES";
                case 1:
                    return "TRAININGS";
            }
            return null;
        }
    }
}
