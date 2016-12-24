package com.example.uilayer.manage;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageTeachersActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    boolean isTeachers;
    @BindView(R.id.viewpager_manage_teachers)
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    ImageView[] indicators;
    int indicatorsCount;
    Drawable selected, nonSelected;
    @BindView(R.id.layout_indicator)
    LinearLayout indicatorLayout;
    @BindView(R.id.button_back)
    Button backButton;
    @BindView(R.id.button_next)
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teachers);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            isTeachers = getIntent().getBooleanExtra("isTeachers", false);
        }
        String title;
        int pagePosition;
        if (isTeachers) {
            pagePosition = 0;
            title = "Teachers";
        } else {
            title = "Sections";
            pagePosition = 1;
        }
        // getSupportActionBar().setTitle("Manage " + title);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), new TeachersSectionsFragment.OptionListener() {
            @Override
            public void onOptionSelected(boolean isTeacher) {
                openBottomSheet(isTeacher);
            }
        });
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        setupPagerIndicatorWithPosition(pagePosition);
    }

    void openBottomSheet(boolean isTeacher) {
        String msg;
        if (isTeacher)
            msg = "open teacher";
        else
            msg = "open section";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    void setupPagerIndicatorWithPosition(int position) {
        indicatorsCount = pagerAdapter.getCount();
        indicators = new ImageView[indicatorsCount];
        selected = getResources().getDrawable(R.drawable.indicator_selected);
        nonSelected = getResources().getDrawable(R.drawable.indicator_non_selected);
        for (int i = 0; i < indicatorsCount; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(nonSelected);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);

            indicatorLayout.addView(indicators[i], params);
        }

        indicators[position].setImageDrawable(selected);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < indicatorsCount; i++) {
            indicators[i].setImageDrawable(nonSelected);
        }
        indicators[position].setImageDrawable(selected);
        //Changing back button and next button states
        boolean stateBack, stateNext;
        if (position == 0) {
            stateBack = false;
            stateNext = true;
        } else if (position == indicatorsCount - 1) {
            stateBack = true;
            stateNext = false;
        } else {
            stateBack = true;
            stateNext = true;
        }
        backButton.setEnabled(stateBack);
        nextButton.setEnabled(stateNext);

    }

    public static class TeachersSectionsFragment extends Fragment {

        private static final String IS_TEACHER = "is_teacher";
        static OptionListener optionListener;
        @BindView(R.id.recycler_fragment_teachers_sections)
        RecyclerView recyclerView;
        @BindView(R.id.toolbar_manage)
        Toolbar toolbar;
        RecyclerView.Adapter adapter;

        public TeachersSectionsFragment() {


        }

        public static TeachersSectionsFragment newInstance(boolean isTeacher, OptionListener optionListener1) {
            TeachersSectionsFragment fragment = new TeachersSectionsFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_TEACHER, isTeacher);
            fragment.setArguments(args);
            optionListener = optionListener1;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_teachers_sections, container, false);
            ButterKnife.bind(this, rootView);
            TextView title = (TextView) toolbar.findViewById(R.id.text_title_toolbar);
            ImageButton addButton = (ImageButton) toolbar.findViewById(R.id.button_add_toolbar);
            ImageButton backButton = (ImageButton) toolbar.findViewById(R.id.button_back_toolbar);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));

            String titleString;
            final Boolean isTeacher = getArguments().getBoolean(IS_TEACHER);
            if (isTeacher) {
                adapter = new TeachersAdapter(getContext(), getTeachers());
                titleString = "Teacher";
            } else {
                adapter = new SectionsAdapter(getContext(), getSections(), 4);
                titleString = "Sections";
            }


            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    optionListener.onOptionSelected(isTeacher);
                }
            });
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
            User user = new User();
            user.setName("AAA");
            user.setPhoneNum("1234567890");
            userArrayList.add(user);
            User user1 = new User();
            user1.setName("BBB");
            user1.setPhoneNum("1234567890");
            userArrayList.add(user1);
            User user2 = new User();
            user2.setName("CCC");
            user2.setPhoneNum("1234567890");
            userArrayList.add(user2);
            return userArrayList;
        }

        interface OptionListener {
            void onOptionSelected(boolean isTeacher);
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        TeachersSectionsFragment.OptionListener optionsListener;

        public PagerAdapter(FragmentManager fm, TeachersSectionsFragment.OptionListener optionsListener) {
            super(fm);
            this.optionsListener = optionsListener;
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

            return TeachersSectionsFragment.newInstance(isTeachers, optionsListener);
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
