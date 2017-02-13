package com.wowconnect.ui.manage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.R;
import com.wowconnect.models.DbUser;
import com.wowconnect.models.Sections;
import com.wowconnect.ui.adapters.SectionsAdapter;
import com.wowconnect.ui.customUtils.HorizontalSpaceItemDecoration;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.ui.helpers.DialogHelper;
import com.wowconnect.ui.helpers.S2mAlertDialog;
import com.wowconnect.ui.landing.LandingActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ManageTeachersActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static String DONE = "Done";
    private static String NEXT = "Next";
    boolean isTeachers, isFirstTime;
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

    @BindView(R.id.activity_manage_teachers)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.layout_bottom_bar)
    RelativeLayout stepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teachers);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            isTeachers = getIntent().getBooleanExtra("isTeachers", false);
            isFirstTime = getIntent().getBooleanExtra("isFirstTime", false);
        }
        int pagePosition;
        pagePosition = isTeachers ? 0 : 1;
        stepperLayout.setVisibility(isFirstTime ? View.VISIBLE : View.GONE);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
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
                if (nextButton.getText().toString().equals(DONE)) {
                    if (isFirstTime) {
                        launchLanding();
                        finish();
                    } else
                        finish();
                } else if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                    pagerAdapter.notifyDataSetChanged();
                }
            }
        });

        if (viewPager.getCurrentItem() == 0) {
            backButton.setEnabled(false);
            updateButtonText(backButton);
        }
    }

    final void openAddTeacherFragment(boolean isUpdate, int position, AddOrUpdateListener listener) {
        AddTeachersFragment bottomSheetDialogFragment = AddTeachersFragment.getNewInstance(isUpdate, position, listener);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    final void openAddSectionsFragment(boolean isUpdate, int position, AddOrUpdateListener listener) {
        AddSectionsFragment bottomSheetDialogFragment = AddSectionsFragment.getNewInstance(isUpdate, position, listener);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    void launchLanding() {
        startActivity(new Intent(ManageTeachersActivity.this, LandingActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        pagerAdapter.removeListeners();
        super.onDestroy();
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
            params.gravity = Gravity.CENTER;
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
            nextButton.setText(NEXT);
        } else if (position == indicatorsCount - 1) {
            stateBack = true;
            stateNext = true;
            nextButton.setText(DONE);
        } else {
            stateBack = true;
            stateNext = true;
            nextButton.setText(NEXT);
        }
        backButton.setEnabled(stateBack);
        nextButton.setEnabled(stateNext);
        updateButtonText(backButton);
        updateButtonText(nextButton);
    }

    void updateButtonText(Button button) {
        if (button.isEnabled())
            button.setTextColor(getResources().getColor(R.color.colorPrimary));
        else
            button.setTextColor(getResources().getColor(R.color.green5));
    }

    public static class TeachersSectionsFragment extends Fragment {

        private static final String IS_TEACHER = "is_teacher";
        private static final String IS_FIRST_TIME = "isFirstTime";
        static TeacherOrSectionListener teacherListener;
        @BindView(R.id.recycler_fragment_teachers_sections)
        RecyclerView recyclerView;
        @BindView(R.id.toolbar_manage)
        Toolbar toolbar;
        static RecyclerView.Adapter adapter;
        Boolean isTeacher, isFirstTime;
        VolleyStringRequest teacherRequest;
        VolleyStringRequest sectionsRequest;

        public TeachersSectionsFragment() {
        }

        public static TeachersSectionsFragment newInstance(boolean isTeacher, boolean isFirstTime) {
            TeachersSectionsFragment fragment = new TeachersSectionsFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_TEACHER, isTeacher);
            args.putBoolean(IS_FIRST_TIME, isFirstTime);
            fragment.setArguments(args);
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
            if (getArguments() != null) {
                isTeacher = getArguments().getBoolean(IS_TEACHER);
                isFirstTime = getArguments().getBoolean(IS_FIRST_TIME);
            }
            if (!isFirstTime) {
                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                });
            } else
                backButton.setVisibility(View.GONE);


            int spanCount;
            String titleString;
            if (isTeacher) {
                //adapter = new TeachersAdapter(getContext(), getTeachers(), 5);
                spanCount = 3;
                loadTeachers();
                titleString = "Teacher";
            } else {
                /*adapter = new SectionsAdapter(getContext(), getUserSections(), 4);
                recyclerView.setAdapter(adapter);*/
                spanCount = 2;
                loadSections();
                titleString = "Sections";
            }
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 5, 3, 2));

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //teacherListener.onAddOptionSelected(isTeacher);
                    if (isTeacher)
                        ((ManageTeachersActivity) getActivity()).openAddTeacherFragment(false, -1, new AddOrUpdateListener() {
                            @Override
                            public void onFinish(boolean isTeacher) {
                                loadTeachers();
                                recyclerView.getAdapter().notifyDataSetChanged();
                                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                            }
                        });
                    else
                        ((ManageTeachersActivity) getActivity()).openAddSectionsFragment(false, -1, new AddOrUpdateListener() {
                            @Override
                            public void onFinish(boolean isTeacher) {
                                loadSections();
                                recyclerView.getAdapter().notifyDataSetChanged();
                                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                            }
                        });
                }
            });
            title.setText("Manage " + titleString);

            return rootView;
        }

        ArrayList<DbUser> teachersList = new ArrayList<>();

        void loadTeachers() {
            teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();
            if (teachersList.size() > 0) {
                setAdapterToRecyclerView();
            } else {
                new NetworkHelper(getContext()).getNetworkUsers(new NetworkHelper.NetworkListener() {
                    @Override
                    public void onFinish() {
                        teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();
                        setAdapterToRecyclerView();
                    }
                });
            }
        }

        void setAdapterToRecyclerView() {
            adapter = new TeachersAdapter(getContext(), teachersList, new TeacherOrSectionListener() {
                @Override
                public void onAddOptionSelected(boolean isTeacher) {

                }

                @Override
                public void onEditOptionSelected(boolean isTeacher, int position) {
                    ((ManageTeachersActivity) getActivity()).openAddTeacherFragment(true, position, new AddOrUpdateListener() {
                        @Override
                        public void onFinish(boolean isTeacher) {
                            loadTeachers();
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onDeleteOptionSelected(boolean isTeacher, final int position) {
                    DialogHelper.createAlertDialog(getActivity(),
                            getString(R.string.alert_delete_teacher),
                            getString(R.string.yes),
                            getString(R.string.no),
                            new S2mAlertDialog.AlertListener() {
                                @Override
                                public void onPositive() {
                                    deleteTeacher(position);
                                    loadTeachers();
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                }

                                @Override
                                public void onNegative() {
                                    DialogHelper.getCurrentDialog().dismiss();
                                }
                            }
                    );


                }
            });
            recyclerView.setAdapter(adapter);
        }

        public static RecyclerView.Adapter getAdapter() {
            return adapter;
        }

        void loadSections() {
            new NetworkHelper(getActivity()).getUserSections(new NetworkHelper.NetworkListener() {
                @Override
                public void onFinish() {
                    updateSections();
                }
            });
        }

        void updateSections() {

            ArrayList<Sections> sectionsArrayList = NewDataHolder.getInstance(getContext()).getSectionsList();
            adapter = new SectionsAdapter(getContext(), sectionsArrayList, 2, new TeacherOrSectionListener() {
                @Override
                public void onAddOptionSelected(boolean isTeacher) {

                }

                @Override
                public void onEditOptionSelected(boolean isTeacher, int position) {
                    ((ManageTeachersActivity) getActivity()).openAddSectionsFragment(true, position, new AddOrUpdateListener() {
                        @Override
                        public void onFinish(boolean isTeacher) {
                            loadSections();
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                }

                @Override
                public void onDeleteOptionSelected(boolean isTeacher, final int position) {
                    DialogHelper.createAlertDialog(getActivity(),
                            getString(R.string.alert_delete_section),
                            getString(R.string.yes),
                            getString(R.string.no),
                            new S2mAlertDialog.AlertListener() {
                                @Override
                                public void onPositive() {
                                    deleteSection(position);
                                    loadSections();
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                }

                                @Override
                                public void onNegative() {
                                    DialogHelper.getCurrentDialog().dismiss();
                                }
                            }
                    );


                }
            }, true);
            recyclerView.setAdapter(adapter);

        }

        void deleteTeacher(final int position) {
            new NetworkHelper(getContext()).deleteTeacher(NewDataHolder.getInstance(getContext()).getTeachersList().get(position).getId(),
                    new NetworkHelper.NetworkListener() {
                        @Override
                        public void onFinish() {
                            Utils.getInstance().showToast("Teacher Deleted");
                            loadTeachers();
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });
        }

        void deleteSection(final int position) {
            new NetworkHelper(getContext()).deleteSection(NewDataHolder.getInstance(getContext()).getSectionsList().get(position).getId(),
                    new NetworkHelper.NetworkListener() {
                        @Override
                        public void onFinish() {
                            Utils.getInstance().showToast("Section Deleted");
                            loadSections();
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onDestroyView() {
            if (teacherRequest != null)
                teacherRequest.removeStatusListener();
            if (sectionsRequest != null)
                sectionsRequest.removeStatusListener();
            super.onDestroyView();
        }


        void removeListener() {
            teacherListener = null;
        }


    }

    public class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = TeachersSectionsFragment.newInstance(true, isFirstTime);
                    break;
                case 1:
                    fragment = TeachersSectionsFragment.newInstance(false, isFirstTime);
                    break;
            }
            return fragment;
        }

        void removeListeners() {
            for (int i = 0; i < getCount(); i++) {
                ((TeachersSectionsFragment) getItem(i)).removeListener();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return isFirstTime ? 2 : 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Manage Teachers";
                case 1:
                    return "Manage Sections";
            }
            return null;
        }
    }
}
