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
import static com.example.domainlayer.Constants.ALL_MILESTONES_URL;
import static com.example.domainlayer.Constants.DELETE_TEACHERS_URL_SUFFIX;
import static com.example.domainlayer.Constants.DETAILED_SECTIONS_URL;
import static com.example.domainlayer.Constants.GET_TEACHERS_URL_SUFFIX;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_COMPLETED_MILES;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_EMAIL;
import static com.example.domainlayer.Constants.KEY_FIRST_NAME;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_LAST_NAME;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE_NAME;
import static com.example.domainlayer.Constants.KEY_NAME;
import static com.example.domainlayer.Constants.KEY_PHONE_NUM;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_STUDENT_COUNT;
import static com.example.domainlayer.Constants.KEY_TEACHER_ID;
import static com.example.domainlayer.Constants.KEY_TOTAL_MILES;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.PREFIX_CLASS;
import static com.example.domainlayer.Constants.PREFIX_SECTION;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
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
    UpdateListener updateListener;
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

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), new TeachersSectionsFragment.TeacherListener() {
            @Override
            public void onAddOptionSelected(boolean isTeacher) {
                openBottomSheet(isTeacher, true, -1);
            }

            @Override
            public void onDeleteOptionSelected(boolean isTeacher, int position) {
                deleteTeacher(position);
            }

            @Override
            public void onEditOptionSelected(boolean isTeacher, int position) {
                openBottomSheet(isTeacher, false, position);
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

    void launchLanding() {
        startActivity(new Intent(ManageTeachersActivity.this, LandingActivity.class));
    }

    void deleteTeacher(final int position) {
        final int teacherId = DataHolder.getInstance(this).getTeachersList().get(position).getId();
        teacherDeleteRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL + "2" + DELETE_TEACHERS_URL_SUFFIX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ManageTeachersActivity.this,
                                "Teacher deleted successfully",
                                Toast.LENGTH_SHORT).show();
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
                params.put(KEY_TEACHER_ID, String.valueOf(teacherId));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(teacherDeleteRequest);
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

    void openBottomSheet(boolean isTeacher, boolean isAdd, int position) {
        View sheetInnerLayout;
        String addOrUpdate = "";
        String teacherOrSection = "";
        if (isAdd)
            addOrUpdate = "Create";
        else
            addOrUpdate = "Update";
        if (isTeacher) {
            teacherOrSection = "Teacher";
            //ADD TEACHER
            sheetInnerLayout = getLayoutInflater().inflate(R.layout.bottom_sheet_create_teacher, null);
            final TextView title = (TextView) sheetInnerLayout.findViewById(R.id.text_create_teacher);
            final TextInputEditText firstName = (TextInputEditText) sheetInnerLayout.findViewById(R.id.text_teacher_first_name);
            final TextInputEditText lastName = (TextInputEditText) sheetInnerLayout.findViewById(R.id.text_teacher_last_name);
            final TextInputEditText phoneNum = (TextInputEditText) sheetInnerLayout.findViewById(R.id.text_mobile_number);
            final TextInputEditText email = (TextInputEditText) sheetInnerLayout.findViewById(R.id.text_teacher_email);

            title.setText(addOrUpdate + Constants.SPACE + teacherOrSection);
            Button adddButton = (Button) sheetInnerLayout.findViewById(R.id.button_add_teacher);
            adddButton.setText(addOrUpdate);
            adddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTeacher(firstName.getText().toString(), lastName.getText().toString()
                            , phoneNum.getText().toString(), email.getText().toString());
                }
            });

            if (!isAdd) //fill the fields
            {
                User teacher = DataHolder.getInstance(this).getTeachersList().get(position);
                firstName.setText(teacher.getFirstName());
                lastName.setText(teacher.getLastName());
                phoneNum.setText(teacher.getPhoneNum());
                email.setText(teacher.getEmail());
            }
        } else {
            //ADD SECTION
            teacherOrSection = "Section";
            sheetInnerLayout = getLayoutInflater().inflate(R.layout.bottom_sheet_create_sections, null);
            final TextView title = (TextView) sheetInnerLayout.findViewById(R.id.text_create_sections);
            bs_classNameInput = (TextInputEditText) sheetInnerLayout.findViewById(R.id.text_class_name);
            bs_sectionNameInput = (TextInputEditText) sheetInnerLayout.findViewById(R.id.text_section_name);
            bs_numOfStudentsInput = (TextInputEditText) sheetInnerLayout.findViewById(R.id.text_num_of_stud);
            bs_milestonesSpinner = (PromptSpinner) sheetInnerLayout.findViewById(R.id.spinner_milestones);
            bs_milestonesSpinner.setPrompt("Milestones");
            bs_milestonesSpinner.setOnItemSelectedListener(milestoneSelectedListener);
            bs_teacherSpinner = (PromptSpinner) sheetInnerLayout.findViewById(R.id.spinner_select_teacher);

            title.setText(addOrUpdate + Constants.SPACE + teacherOrSection);
            {//Reduce spinner drop down view height
                android.util.TypedValue value = new android.util.TypedValue();
                getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, value, true);
                android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float ret = value.getDimension(metrics);
                bs_teacherSpinner.setMinimumHeight((int) (ret - 1 * metrics.density));
            }
            bs_teacherSpinner.setPrompt("Teachers");
            bs_teacherSpinner.setOnItemSelectedListener(teacherSelectedListener);
            bs_addTeacherBtn = (Button) sheetInnerLayout.findViewById(R.id.button_add_section);
            bs_addTeacherBtn.setText(addOrUpdate);
            bs_addTeacherBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addSection(bs_classNameInput.getText().toString(),
                            bs_sectionNameInput.getText().toString(),
                            String.valueOf(selectedMilestoneId),
                            String.valueOf(selectedTeacherId),
                            bs_numOfStudentsInput.getText().toString());

                }
            });
            if (milestonesList.size() == 0)
                getMilestones();
            else {
                updateMilesSpinner(milestonesList);
            }

            try {
                Field popup = Spinner.class.getDeclaredField("mPopup");
                popup.setAccessible(true);

                // Get private mPopup member variable and try cast to ListPopupWindow
                android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(bs_milestonesSpinner);
                android.widget.ListPopupWindow popupWindow1 = (android.widget.ListPopupWindow) popup.get(bs_teacherSpinner);

                // Set popupWindow height to 500px
                popupWindow.setHeight(Utils.getInstance().getPixelAsDp(this, 300));
                popupWindow1.setHeight(Utils.getInstance().getPixelAsDp(this, 300));
            } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

            }

            TeachersSpinnerAdapter teacherAdapter = new TeachersSpinnerAdapter(this,
                    R.layout.item_spinner,
                    R.id.text_spinner,
                    (DataHolder.getInstance(this).getTeachersList()));
            //teacherAdapter.setDropDownViewResource(R.layout.item_spinner);
            bs_teacherSpinner.setAdapter(teacherAdapter);

            if (!isAdd) //fill the fields
            {
                Sections sec = DataHolder.getInstance(this).getSectionsList().get(position);
                bs_classNameInput.setText(sec.get_Class());
                bs_sectionNameInput.setText(sec.getSection());
                bs_numOfStudentsInput.setText("" + sec.getNumOfStuds());
            }

        }
        bottomSheetLayout.removeAllViewsInLayout();
        bottomSheetLayout.addView(sheetInnerLayout);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    void updateMilesSpinner(ArrayList<Milestones> milestonesList) {
        MilestonesSpinnerAdapter dataAdapter = new MilestonesSpinnerAdapter(this, R.layout.item_spinner, R.id.text_spinner, milestonesList);
        //dataAdapter.setDropDownViewResource(R.layout.item_spinner);
        bs_milestonesSpinner.setAdapter(dataAdapter);
    }

    void addSection(final String className, final String sectionName, final String milestoneId, final String userId, final String studCOunt) {
        sectionAddRequest = new VolleyStringRequest(Request.Method.POST, ADD_SECTIONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ManageTeachersActivity.this, "Section added successfully", Toast.LENGTH_SHORT).show();
                        Log.d("sectionAddRequest", "response: " + response);
                        bottomSheetCloseButton.performClick();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("sectionAddRequest", "onErrorResponse: " + error);

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
                params.put(KEY_CLASS, className);
                params.put(KEY_SECTION, sectionName);
                params.put(KEY_MILESTONE_ID, milestoneId);
                params.put(KEY_USER_ID, userId);
                params.put(KEY_STUDENT_COUNT, studCOunt);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(sectionAddRequest);
    }

    void getMilestones() {

        milestonesRequest = new VolleyStringRequest(Request.Method.GET, ALL_MILESTONES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("milestonesRequest", "onResponse: " + response);
                        updateMilestones(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.e("milestonesRequest", "onErrorResponse: ", error);

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
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(milestonesRequest);

    }

    void updateMilestones(String response) {
        ArrayList<Milestones> milestonesArrayList = new ArrayList<>();
        try {
            JSONArray milestonesArray = new JSONArray(response);
            for (int i = 0; i < milestonesArray.length(); i++) {
                JSONObject milestone = milestonesArray.getJSONObject(i);
                milestonesArrayList.add(i,
                        new Milestones(milestone.getInt(KEY_ID),
                                milestone.getString(KEY_NAME)));
            }
            milestonesList = new ArrayList<>(milestonesArrayList);
        } catch (JSONException ex) {
            Log.e("updateMilestones", "updateMilestones: ", ex);
        }
        updateMilesSpinner(milestonesList);
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
                        //   ((TeachersSectionsFragment) pagerAdapter.getItem(0)).loadTeachers();
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

    interface UpdateListener {
        void onTeacherUpdate();
    }

    public static class TeachersSectionsFragment extends Fragment implements UpdateListener {

        private static final String IS_TEACHER = "is_teacher";
        static TeacherListener teacherListener;
        @BindView(R.id.recycler_fragment_teachers_sections)
        RecyclerView recyclerView;
        @BindView(R.id.toolbar_manage)
        Toolbar toolbar;
        RecyclerView.Adapter adapter;
        Boolean isTeacher;
        VolleyStringRequest teacherRequest;
        VolleyStringRequest sectionsRequest;
        ArrayList<User> teachersList = new ArrayList<>();

        public TeachersSectionsFragment() {


        }

        public static TeachersSectionsFragment newInstance(boolean isTeacher, TeacherListener teacherListener1) {
            TeachersSectionsFragment fragment = new TeachersSectionsFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_TEACHER, isTeacher);
            fragment.setArguments(args);
            teacherListener = teacherListener1;
            return fragment;
        }

        @Override
        public void onTeacherUpdate() {
            loadTeachers();
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
                    teacherListener.onAddOptionSelected(isTeacher);
                }
            });
            title.setText("Manage " + titleString);

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
            sectionsArrayList.add(new Sections(6, "Class 6", "Section F", 90, 100, 2, "Milestone 6", 6));
            sectionsArrayList.add(new Sections(7, "Class 7", "Section G", 46, 100, 2, "Milestone 7", 7));
            sectionsArrayList.add(new Sections(8, "Class 8", "Section H", 56, 100, 2, "Milestone 8", 8));
            sectionsArrayList.add(new Sections(9, "Class 9", "Section I", 30, 100, 2, "Milestone 9", 9));
            sectionsArrayList.add(new Sections(10, "Class 10", "Section J", 97, 100, 2, "Milestone 10", 10));
            sectionsArrayList.add(new Sections(11, "Class 11", "Section L", 37, 100, 2, "Milestone 11", 11));
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

        public void loadTeachers() {
            teacherRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL + "2" + GET_TEACHERS_URL_SUFFIX,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("teacherRequest", "onResponse: " + response);
                            updateTeachers(response);
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
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

            };
            VolleySingleton.getInstance(getContext()).addToRequestQueue(teacherRequest);

        }

        void loadSections() {
            sectionsRequest = new VolleyStringRequest(Request.Method.GET, DETAILED_SECTIONS_URL,
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new ArrayMap<>();
                    header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                    header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                    return header;
                }
            };

            VolleySingleton.getInstance(getContext()).addToRequestQueue(sectionsRequest);
        }

        @SuppressWarnings("NewApi")
        void updateSections(String teachersResponse) {
            ArrayList<Sections> sectionsArrayList = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(teachersResponse);
                Log.d("ARRAY", "updateSections: " + obj.toString());
                JSONArray sectionsArray = new JSONArray(obj.getString(KEY_SECTIONS));
                for (int i = 0; i < sectionsArray.length(); i++) {
                    JSONObject milesDatObject = sectionsArray.getJSONObject(i);
                    Sections section
                            = new Sections(milesDatObject.getInt(KEY_ID),
                            (PREFIX_CLASS + milesDatObject.getString(KEY_CLASS)),
                            (PREFIX_SECTION + milesDatObject.getString(KEY_SECTION)),
                            milesDatObject.getInt(KEY_COMPLETED_MILES),
                            milesDatObject.getInt(KEY_TOTAL_MILES),
                            milesDatObject.getInt(KEY_SCHOOL_ID),
                            Constants.KEY_MILESTONE_PREFIX + Constants.SPACE + milesDatObject.getString(KEY_MILESTONE_NAME),
                            milesDatObject.getInt(KEY_MILESTONE_ID));
                    sectionsArrayList.add(section);
                    DataHolder.getInstance(getActivity())
                            .setSectionsList(sectionsArrayList);
                }
            } catch (JSONException ex) {
                Log.d("Error", "updateSections: " + ex);
            }
            adapter = new SectionsAdapter(getContext(), sectionsArrayList, 2, teacherListener, true);
            recyclerView.setAdapter(adapter);
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

        void updateTeachers(String teachersResponse) {
            ArrayList<User> teachersList = new ArrayList<>();
            try {
                JSONArray profilesArray = new JSONArray(teachersResponse);
                for (int i = 0; i < profilesArray.length(); i++) {
                    JSONObject userJson = profilesArray.getJSONObject(i);
                    User user = new User();
                    user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
                    user.setId(userJson.getInt(Constants.KEY_ID));
                    user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
                    //user.setEmail(userJson.getString(Constants.KEY_EMAIL));
                    user.setPhoneNum(userJson.getString(Constants.KEY_PHONE_NUM));
                    //user.setLastLogin(userJson.getString(Constants.KEY_LAST_LOGIN));
                    //user.setSchoolId(userJson.getInt(Constants.KEY_SCHOOL_ID));
                    //user.setSchoolName(userJson.getString(Constants.KEY_SCHOOL_NAME));

                    // user.setWow(userJson.getString(Constants.KEY_WOW));
                    user.setAvatar(userJson.getString(Constants.KEY_AVATAR));
                    // user.setMiles(userJson.getString(Constants.KEY_MILES));
                    // user.setTrainings(userJson.getString(Constants.KEY_TRAINING));
                    // user.setUserType(userJson.getString(Constants.KEY_TYPE));
                    teachersList.add(i, user);
                }

                DataHolder.getInstance(getContext()).setTeachersList(teachersList);
                adapter = new TeachersAdapter(getContext(), teachersList, 5, teacherListener);
                recyclerView.setAdapter(adapter);
            } catch (JSONException exception) {
                Log.e("DataHolder", "saveUserDetails: ", exception);
            }
        }

        public ArrayList<User> getTeachersList() {
            return teachersList;
        }

        void removeListener() {
            teacherListener = null;
        }

        public interface TeacherListener {
            void onAddOptionSelected(boolean isTeacher);

            void onEditOptionSelected(boolean isTeacher, int position);

            void onDeleteOptionSelected(boolean isTeacher, int position);
        }

    }

    public class PagerAdapter extends FragmentPagerAdapter {
        TeachersSectionsFragment.TeacherListener optionsListener;

        public PagerAdapter(FragmentManager fm, TeachersSectionsFragment.TeacherListener optionsListener) {
            super(fm);
            this.optionsListener = optionsListener;
        }

        @Override
        public Fragment getItem(int position) {
            boolean isTeachers = false;
            Fragment fragment = null;
            switch (position) {
                case 0:
                    isTeachers = true;
                    // teachersFragment = TeachersSectionsFragment.newInstance(true, optionsListener);
                    fragment = TeachersSectionsFragment.newInstance(true, optionsListener);
                    break;
                case 1:
                    isTeachers = false;
                    //sectionsFragment = TeachersSectionsFragment.newInstance(false, optionsListener);
                    fragment = TeachersSectionsFragment.newInstance(false, optionsListener);
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
