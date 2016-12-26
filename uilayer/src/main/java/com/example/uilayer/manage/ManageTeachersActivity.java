package com.example.uilayer.manage;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.zip.Inflater;

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
    @BindView(R.id.layout_bottom_sheet)
    RelativeLayout bottomSheetLayout;
    @BindView(R.id.bottom_sheet_manage)
    FrameLayout bottomSheet;
    @BindView(R.id.layout_bottom_sheet_inner)
    RelativeLayout bottomSheetInnerLayout;
    @BindView(R.id.close_icon)
    ImageButton bottomSheetCloseButton;
    BottomSheetBehavior<FrameLayout> behavior;
    @BindView(R.id.activity_manage_teachers)
    CoordinatorLayout coordinatorLayout;

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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(0);
                    pagerAdapter.notifyDataSetChanged();
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                    pagerAdapter.notifyDataSetChanged();
                }
            }
        });
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);



            }
        });
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
           behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        super.onBackPressed();
    }

    void openBottomSheet(boolean isTeacher) {
        int innerLayoutId;

        String msg;
        if (isTeacher) {
            msg = "open teacher";
            innerLayoutId = R.layout.bottom_sheet_create_teacher;
        } else {
            msg = "open section";
            innerLayoutId = R.layout.bottom_sheet_create_sections;
        }
        View sheetInnerLayout = getLayoutInflater().inflate(innerLayoutId, null);
        bottomSheetLayout.removeAllViewsInLayout();
        bottomSheetLayout.addView(sheetInnerLayout);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        bottomSheet.getLayoutParams().height = bottomSheetLayout.getHeight();
        bottomSheet.requestLayout();

        behavior.onLayoutChild(coordinatorLayout, bottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
     //   behavior.setPeekHeight(bottomSheetInnerLayout.getHeight());
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

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
            // recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));
            recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 5, 5, 3));
            String titleString;
            final Boolean isTeacher = getArguments().getBoolean(IS_TEACHER);
            if (isTeacher) {
                adapter = new TeachersAdapter(getContext(), getTeachers(), 4);

                titleString = "Teacher";
            } else {
                adapter = new SectionsAdapter(getContext(), getSections(), 4);
                //  recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));
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
            sectionsArrayList.add(new Sections(1, "Class 1", "Section A", 25, 100, 2, "Milestone 1", 1));
            sectionsArrayList.add(new Sections(2, "Class 2", "Section B", 30, 100, 2, "Milestone 2", 2));
            sectionsArrayList.add(new Sections(3, "Class 3", "Section C", 40, 100, 2, "Milestone 3", 3));
            sectionsArrayList.add(new Sections(4, "Class 4", "Section D", 63, 100, 2, "Milestone 4", 4));
            sectionsArrayList.add(new Sections(5, "Class 5", "Section E", 30, 100, 2, "Milestone 5", 5));
            sectionsArrayList.add(new Sections(6, "Class 6", "Section F", 90, 100, 2, "Milestone 6", 6));
            sectionsArrayList.add(new Sections(7, "Class 7", "Section G", 46, 100, 2, "Milestone 7", 7));
            sectionsArrayList.add(new Sections(8, "Class 8", "Section H", 56, 100, 2, "Milestone 8", 8));
            sectionsArrayList.add(new Sections(9, "Class 9", "Section I", 30, 100, 2, "Milestone 9", 9));
            sectionsArrayList.add(new Sections(10, "Class 10", "Section J", 97, 100, 2, "Milestone 10", 10));
            sectionsArrayList.add(new Sections(11, "Class 11", "Section L", 37, 100, 2, "Milestone 11", 11));
            return sectionsArrayList;
        }

        ArrayList<User> getTeachers() {
            ArrayList<User> userArrayList = new ArrayList<>();
            User user = new User();
            user.setFirstName("AAA");
            user.setLastName("AAA");
            user.setPhoneNum("1234567890");
            user.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
            userArrayList.add(user);
            User user1 = new User();
            user1.setFirstName("BBB");
            user1.setLastName("BBB");
            user1.setPhoneNum("1234567890");
            user1.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
            userArrayList.add(user1);
            User user2 = new User();
            user2.setFirstName("CCC");
            user2.setLastName("CCC");
            user2.setPhoneNum("1234567890");
            user2.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
            userArrayList.add(user2);
            User user3 = new User();
            user3.setFirstName("AAA");
            user3.setLastName("AAA");
            user3.setPhoneNum("1234567890");
            user3.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
            userArrayList.add(user3);
            User user4 = new User();
            user4.setFirstName("BBB");
            user4.setLastName("BBB");
            user4.setPhoneNum("1234567890");
            user4.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
            userArrayList.add(user4);
            User user5 = new User();
            user5.setFirstName("CCC");
            user5.setLastName("CCC");
            user5.setPhoneNum("1234567890");
            user5.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
            userArrayList.add(user5);
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
