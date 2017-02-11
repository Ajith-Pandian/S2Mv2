package com.example.uilayer.manage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataParser;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.landing.LandingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.SEPERATOR;


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


        void loadTeachers() {
            new NetworkHelper(getContext()).getTeachers(new NetworkHelper.NetworkListener() {
                @Override
                public void onFinish() {
                    adapter = new TeachersAdapter(getContext(), NewDataHolder.getInstance(getContext()).getTeachersList(), 5, new TeacherOrSectionListener() {
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
                        public void onDeleteOptionSelected(boolean isTeacher, int position) {
                            deleteTeacher(position);
                            loadTeachers();
                            recyclerView.getAdapter().notifyDataSetChanged();

                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            });
        }

        public static RecyclerView.Adapter getAdapter() {
            return adapter;
        }

        void loadSections() {
            sectionsRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR + KEY_SECTIONS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Sections", "onErrorResponse: " + response);
                            updateSections(response);
                        }
                    },
                    new VolleyStringRequest.VolleyErrListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            super.onErrorResponse(error);
                            Log.d("Sections", "onErrorResponse: " + error);
                        }
                    }, new VolleyStringRequest.StatusCodeListener() {
                String TAG = "VolleyStringReq";

                @Override
                public void onBadRequest() {
                    Log.d(TAG, "onBadRequest: ");
                }

                @Override
                public void onUnauthorized() {
                    Log.d(TAG, "onUnauthorized: ");
                }

                @Override
                public void onNotFound() {
                    Log.d(TAG, "onNotFound: ");
                }


                @Override
                public void onConflict() {
                    Log.d(TAG, "onConflict: ");
                }

                @Override
                public void onTimeout() {
                    Log.d(TAG, "onTimeout: ");
                }
            });

            VolleySingleton.getInstance(getContext()).addToRequestQueue(sectionsRequest);
        }

        @SuppressWarnings("NewApi")
        void updateSections(String teachersResponse) {
            try {
                JSONObject obj = new JSONObject(teachersResponse);
                Log.d("ARRAY", "updateSections: " + obj.toString());
                JSONArray sectionsArray = new JSONArray(obj.getString(KEY_SECTIONS));
                ArrayList<Sections> sectionsArrayList = new DataParser().getSectionsListFromJson(sectionsArray, false);

                NewDataHolder.getInstance(getContext()).setSectionsList(sectionsArrayList);
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
                    public void onDeleteOptionSelected(boolean isTeacher, int position) {
                        deleteSection(position);
                        loadSections();
                        recyclerView.getAdapter().notifyDataSetChanged();

                    }
                }, true);
                recyclerView.setAdapter(adapter);
            } catch (JSONException ex) {
                Log.d("Error", "updateSections: " + ex);
            }
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
            return 2;
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
