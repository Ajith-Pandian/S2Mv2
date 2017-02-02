package com.example.uilayer.milestones;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.domainlayer.models.milestones.TMiles;
import com.example.uilayer.DataHolder;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.MilesAdapter;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Milestones;
import com.example.uilayer.milestones.betterAdapter.model.Training;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.TYPE_MILE;
import static com.example.domainlayer.Constants.TYPE_TRAINING;

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
    TabLayout tabLayout;
    TextView toolbarTitle, toolbarSubTitle;
    ImageButton backButton;
    NewDataHolder dataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_archive);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.text_title_toolbar);
        toolbarSubTitle = (TextView) toolbar.findViewById(R.id.text_subtitle_toolbar);
        backButton = (ImageButton) toolbar.findViewById(R.id.button_back_toolbar);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        dataHolder = NewDataHolder.getInstance(getApplicationContext());
        toolbarTitle.setText("Archive");
        toolbarSubTitle.setText(dataHolder.getCurrentClassName() + " " + dataHolder.getCurrentSectionName());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Set up the Tablayout with the view pager
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);


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
        MilesAdapter milestonesAdapter;

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
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(archiveRecycler.getContext(),
                    mLayoutManager.getOrientation());
            boolean isTraining;
            isTraining = getArguments().getBoolean(IS_MILE);
            archiveRecycler.addItemDecoration(mDividerItemDecoration);
            milestonesAdapter = new MilesAdapter(getContext(), getMilesOrTrainingsList(isTraining),
                    DataHolder.getInstance(getActivity()).getUndoableId());
            archiveRecycler.setAdapter(milestonesAdapter);
            return rootView;
        }

        ArrayList<TMiles> getMilesOrTrainingsList(boolean isMile) {
            String type = "";
            if (isMile)
                type = TYPE_MILE;
            else
                type = TYPE_TRAINING;

            ArrayList<TMiles> actualList = DataHolder.getInstance(getActivity()).getArchiveData();
            ArrayList<TMiles> filteredList = new ArrayList<>();
            for (int i = 0; i < actualList.size(); i++) {
                if (actualList.get(i).getType().equals(type))
                    filteredList.add(actualList.get(i));
            }
            Collections.reverse(filteredList);
            return filteredList;
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
            boolean type;
            switch (position) {
                case 0:
                    type = true;
                    break;
                case 1:
                    type = false;
                    break;
                default:
                    type = true;
                    break;

            }
            return PlaceholderFragment.newInstance(type);
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
