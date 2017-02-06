package com.example.uilayer.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Milestones;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataHolder;
import com.example.domainlayer.temp.DataParser;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.adapters.MilestonesSpinnerAdapter;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.adapters.TeachersSpinnerAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.customUtils.views.PromptSpinner;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.landing.LandingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.ADD_SECTIONS_URL;
import static com.example.domainlayer.Constants.ADD_TEACHERS_URL_SUFFIX;
import static com.example.domainlayer.Constants.DELETE_TEACHERS_URL_SUFFIX;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_EMAIL;
import static com.example.domainlayer.Constants.KEY_FIRST_NAME;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_LAST_NAME;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_NAME;
import static com.example.domainlayer.Constants.KEY_PHONE_NUM;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_STUDENT_COUNT;
import static com.example.domainlayer.Constants.KEY_TEACHERS;
import static com.example.domainlayer.Constants.KEY_TEACHER_ID;
import static com.example.domainlayer.Constants.MILESTONES_URL;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

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
    @BindView(R.id.layout_dummy_frame_manage)
    FrameLayout dummyLayout;
    @BindView(R.id.layout_bottom_bar)
    RelativeLayout stepperLayout;
    VolleyStringRequest teacherAddRequest, teacherDeleteRequest;
    VolleyStringRequest sectionAddRequest;
    VolleyStringRequest milestonesRequest;
    TextInputEditText bs_classNameInput, bs_sectionNameInput, bs_numOfStudentsInput;
    Button bs_addTeacherBtn;
    PromptSpinner bs_milestonesSpinner, bs_teacherSpinner;
    int selectedMilestoneId = -1, selectedTeacherId = -1;
    AdapterView.OnItemSelectedListener milestoneSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("view.getId", "onItemSelected: " + view.getId());

            selectedMilestoneId = ((Milestones) parent.getItemAtPosition(position)).getId();
            Log.d("selectedMilestoneId", "onItemSelected: " + selectedMilestoneId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
    AdapterView.OnItemSelectedListener teacherSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedTeacherId = ((User) parent.getItemAtPosition(position)).getId();
            Log.d("selectedTeacherId", "onItemSelected: " + selectedTeacherId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
    ArrayList<Milestones> milestonesList = new ArrayList<>();

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
                    if (isFirstTime)
                        launchLanding();
                    else
                        finish();
                } else if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                    pagerAdapter.notifyDataSetChanged();
                }
            }
        });
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        dummyLayout.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset < 0) {
                    float alpha = (1 + slideOffset) * 200;
                    dummyLayout.getForeground().setAlpha((int) (alpha));
                    dummyLayout.setVisibility(View.VISIBLE);
                }
                if (slideOffset == -1)
                    closeBottomSheet();


            }
        });
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        dummyLayout.getForeground().setAlpha(0);
        dummyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        if (viewPager.getCurrentItem() == 0) {
            backButton.setEnabled(false);
            updateButtonText(backButton);
        }
    }

    final void openAddTeacherFragment(boolean isUpdate, int position,AddOrUpdateListener listener) {
        AddTeachersFragment bottomSheetDialogFragment = AddTeachersFragment.getNewInstance(isUpdate, position,listener);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    final void openAddSectionsFragment(boolean isUpdate, int position, AddOrUpdateListener listener) {
        AddSectionsFragment bottomSheetDialogFragment = AddSectionsFragment.getNewInstance(isUpdate, position, listener);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    void launchLanding() {
        startActivity(new Intent(ManageTeachersActivity.this, LandingActivity.class));
    }



    void closeBottomSheet() {
        InputMethodManager keyboard = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
        //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else finish();

    }

    @Override
    protected void onDestroy() {
        pagerAdapter.removeListeners();
        super.onDestroy();
    }


    void updateMilesSpinner(ArrayList<Milestones> milestonesList) {
        MilestonesSpinnerAdapter dataAdapter = new MilestonesSpinnerAdapter(this, R.layout.item_spinner, R.id.text_spinner, milestonesList);
        //dataAdapter.setDropDownViewResource(R.layout.item_spinner);
        bs_milestonesSpinner.setAdapter(dataAdapter);
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
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    void updateButtonText(Button button) {
        if (button.isEnabled())
            button.setTextColor(getResources().getColor(R.color.colorPrimary));
        else
            button.setTextColor(getResources().getColor(R.color.green5));
    }

    void addTeacher(final String firstName, final String lastName, final String phoneNum, final String email) {
        // void addTeacher() {
        teacherAddRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL + "2" + ADD_TEACHERS_URL_SUFFIX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ManageTeachersActivity.this, "Teacher added successfully", Toast.LENGTH_SHORT).show();
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        //   ((TeachersSectionsFragment) pagerAdapter.getItem(0)).getTeachers();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("teacherRequest", "onErrorResponse: " + error);

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
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }


            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(KEY_FIRST_NAME, firstName);
                params.put(KEY_LAST_NAME, lastName);
                //  if()
                params.put(KEY_EMAIL, email);
                params.put(KEY_PHONE_NUM, phoneNum);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(teacherAddRequest);
    }


    public static class TeachersSectionsFragment extends Fragment  {

        private static final String IS_TEACHER = "is_teacher";
        static TeacherOrSectionListener teacherListener;
        @BindView(R.id.recycler_fragment_teachers_sections)
        RecyclerView recyclerView;
        @BindView(R.id.toolbar_manage)
        Toolbar toolbar;
        static RecyclerView.Adapter adapter;
        Boolean isTeacher;
        VolleyStringRequest teacherRequest;
        VolleyStringRequest sectionsRequest;
        ArrayList<User> teachersList = new ArrayList<>();

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
            recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 5, 3, 3));
            String titleString;
            isTeacher = getArguments().getBoolean(IS_TEACHER);
            if (isTeacher) {
                //adapter = new TeachersAdapter(getContext(), getTeachers(), 5);
                loadTeachers();
                titleString = "Teacher";
            } else {
                /*adapter = new SectionsAdapter(getContext(), getSections(), 4);
                recyclerView.setAdapter(adapter);*/
                loadSections();
                titleString = "Sections";
            }


            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //teacherListener.onAddOptionSelected(isTeacher);
                    if (isTeacher)
                        ((ManageTeachersActivity) getActivity()).openAddTeacherFragment(false, -1,new AddOrUpdateListener() {
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
        //TeacherOrSectionListener optionsListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            //this.optionsListener = optionsListener;
        }

        @Override
        public Fragment getItem(int position) {
            boolean isTeachers = false;
            Fragment fragment = null;
            switch (position) {
                case 0:
                    isTeachers = true;
                    // teachersFragment = TeachersSectionsFragment.newInstance(true, optionsListener);
                    fragment = TeachersSectionsFragment.newInstance(true);
                    break;
                case 1:
                    isTeachers = false;
                    //sectionsFragment = TeachersSectionsFragment.newInstance(false, optionsListener);
                    fragment = TeachersSectionsFragment.newInstance(false);
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
                    return "MILES";
                case 1:
                    return "TRAININGS";
            }
            return null;
        }
    }
}
