package com.example.uilayer.manage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.milestones.ArchiveActivity;
import com.example.uilayer.milestones.adapters.MilesAdapter;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageTeachersActivity extends AppCompatActivity {
    boolean isTeachers;
    @BindView(R.id.viewpager_manage_teachers)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teachers);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            isTeachers = getIntent().getBooleanExtra("isTeachers", false);
        }
        String title;
        if (isTeachers) {
            title = "Teachers";
        } else {
            title = "Sections";
        }
        // getSupportActionBar().setTitle("Manage " + title);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

    }

    public static class TeachersSectionsFragment extends Fragment {

        private static final String IS_TEACHER = "is_teacher";
        @BindView(R.id.recycler_fragment_teachers_sections)
        RecyclerView recyclerView;
        @BindView(R.id.toolbar_manage)
        Toolbar toolbar;
        RecyclerView.Adapter adapter;

        public TeachersSectionsFragment() {
        }

        public static TeachersSectionsFragment newInstance(boolean isTeacher) {
            TeachersSectionsFragment fragment = new TeachersSectionsFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_TEACHER, isTeacher);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_teachers_sections, container, false);
            ButterKnife.bind(this, rootView);
            TextView title = (TextView) toolbar.findViewById(R.id.text_title_toolbar);

            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));

            String titleString;
            if (getArguments().getBoolean(IS_TEACHER)) {
                adapter = new TeachersAdapter(getContext(), getTeachers());
                titleString = "Teacher";
            } else {
                adapter = new SectionsAdapter(getContext(), getSections());
                titleString = "Sections";
            }

            title.setText("Manage " + titleString);
            recyclerView.setAdapter(adapter);
            return rootView;
        }

        ArrayList<Sections> getSections() {
            ArrayList<Sections> sectionsArrayList = new ArrayList<>();
            sectionsArrayList = new ArrayList<>();
            sectionsArrayList.add(new Sections(1, "Class 1", "Section A", 25, 100));
            sectionsArrayList.add(new Sections(2, "Class 2", "Section B", 30, 100));
            sectionsArrayList.add(new Sections(3, "Class 3", "Section C", 40, 100));
            sectionsArrayList.add(new Sections(4, "Class 4", "Section D", 63, 100));
            sectionsArrayList.add(new Sections(5, "Class 5", "Section E", 30, 100));
            sectionsArrayList.add(new Sections(6, "Class 6", "Section F", 90, 100));
            sectionsArrayList.add(new Sections(7, "Class 7", "Section G", 46, 100));
            sectionsArrayList.add(new Sections(8, "Class 8", "Section H", 56, 100));
            sectionsArrayList.add(new Sections(9, "Class 9", "Section I", 30, 100));
            sectionsArrayList.add(new Sections(10, "Class 10", "Section J", 97, 100));
            sectionsArrayList.add(new Sections(11, "Class 11", "Section L", 37, 100));
            return sectionsArrayList;
        }

        ArrayList<User> getTeachers() {
            ArrayList<User> userArrayList = new ArrayList<>();
            return userArrayList;
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            boolean isTeachers = false;
            switch (position) {
                case 0:
                    isTeachers = true;
                    break;
                case 1:
                    isTeachers = false;
                    break;
            }

            return TeachersSectionsFragment.newInstance(isTeachers);
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
