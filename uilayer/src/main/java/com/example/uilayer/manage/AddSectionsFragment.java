package com.example.uilayer.manage;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.R;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.adapters.MilestonesSpinnerAdapter;
import com.example.uilayer.adapters.TeachersSpinnerAdapter;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.customUtils.views.PromptSpinner;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.ADD_SECTIONS_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ASSIGN;
import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_COUNTRY_CODE;
import static com.example.domainlayer.Constants.KEY_CREATE;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_EMAIL;
import static com.example.domainlayer.Constants.KEY_FIRST_NAME;
import static com.example.domainlayer.Constants.KEY_LAST_NAME;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_PHONE_NUM;
import static com.example.domainlayer.Constants.KEY_ROLES;
import static com.example.domainlayer.Constants.KEY_ROLES_ARRAY;
import static com.example.domainlayer.Constants.KEY_SCHOOL;
import static com.example.domainlayer.Constants.KEY_SCHOOLS;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_STUDENT_COUNT;
import static com.example.domainlayer.Constants.KEY_UPDATE;
import static com.example.domainlayer.Constants.KEY_USERS;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.KEY_USER_TYPE;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TYPE_TEACHER;

/**
 * Created by thoughtchimp on 1/4/2017.
 */

public class AddSectionsFragment extends BottomSheetDialogFragment {
    private static final String IS_UPDATE = "isUpdate";
    private static final String POSITION = "position";
    static AddSectionsFragment newInstance;
    @BindView(R.id.text_create_sections)
    TextView title;
    @BindView(R.id.text_class_name)
    TextInputEditText className;
    @BindView(R.id.text_section_name)
    TextInputEditText sectionName;
    @BindView(R.id.text_num_of_stud)
    TextInputEditText numOfStudents;
    @BindView(R.id.spinner_milestones)
    PromptSpinner milestoneSpinner;
    @BindView(R.id.spinner_select_teacher)
    PromptSpinner teachersSpinner;
    @BindView(R.id.close_icon)
    ImageButton closeButton;
    @BindView(R.id.button_add_section)
    Button addButton;

    boolean isUpdate;
    int position;
    VolleyStringRequest getUsersRequest;
    BottomSheetBehavior bottomSheetBehavior;
    Dialog thisDialog;
    VolleyStringRequest createTicketRequest;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public static AddSectionsFragment getNewInstance(boolean isUpdate, int position) {
        newInstance = new AddSectionsFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_UPDATE, isUpdate);
        args.putInt(POSITION, position);
        newInstance.setArguments(args);
        return newInstance;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(params);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isUpdate = getArguments().getBoolean(IS_UPDATE);
            position = getArguments().getInt(POSITION, -1);
        }

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_add_section, null);
        dialog.setContentView(contentView);
        thisDialog = dialog;
        ButterKnife.bind(this, contentView);
        initViews();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = ((BottomSheetBehavior) behavior);
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
            bottomSheetBehavior.setPeekHeight(Utils.getInstance().getPixelAsDp(dialog.getContext(), 330));
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING:
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        }
    }

    String getTextFromView(EditText editText) {
        return editText.getText().toString();
    }

    int getSelectedSectionId() {
        return NewDataHolder.getInstance(getContext()).getSectionsList().get(this.position).getId();
    }

    int getSpinnerTeacherId() {
        return ((User) teachersSpinner.getSelectedItem()).getId();

    }

    int getSpinnerMilestoneId() {
        return ((Milestones) milestoneSpinner.getSelectedItem()).getId();
    }

    void initViews() {
        initSpinners();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                thisDialog.dismiss();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUpdate) {
                    updateSections(getSelectedSectionId(),
                            getTextFromView(className),
                            getTextFromView(sectionName),
                            String.valueOf(getSpinnerMilestoneId()),
                            String.valueOf(getSpinnerTeacherId()),
                            getTextFromView(numOfStudents)
                    );
                } else {
                    addSection(getTextFromView(className),
                            getTextFromView(sectionName),
                            String.valueOf(getSpinnerMilestoneId()),
                            String.valueOf(getSpinnerTeacherId()),
                            getTextFromView(numOfStudents));
                }
            }
        });

        if (isUpdate) {
            Sections section = NewDataHolder.getInstance(getContext()).getSectionsList().get(position);
            className.setText(section.get_Class());
            sectionName.setText(section.getSection());
            selectMilestone(section.getMilestoneId());
            selectTeacher(section.getTeacherId());
            numOfStudents.setText(String.valueOf(section.getNumOfStuds()));
            addButton.setText("Update");
            title.setText("Update Sections");
        }
    }

    void selectMilestone(int milestoneId) {
        ArrayList<Milestones> milestonesList = new NewDataParser().getS2mConfiguration().getMilestonesArrayList();

        for (int i = 0; i < milestonesList.size(); i++) {
            if (milestoneId == milestonesList.get(i).getId())
                milestoneSpinner.setSelection(i);
        }
    }
    void selectTeacher(int teacherId) {
        ArrayList<User> teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();

        for (int i = 0; i < teachersList.size(); i++) {
            if (teacherId == teachersList.get(i).getId())
                teachersSpinner.setSelection(i);
        }
    }


    void initSpinners() {
        //Teacher spinner
        teachersSpinner.setPrompt("Teacher");
        ArrayList<User> teachersList = new ArrayList<>();
        if (NewDataHolder.getInstance(getContext()).getTeachersList() != null) {
            teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();
        }
        TeachersSpinnerAdapter teacherAdapter = new TeachersSpinnerAdapter(getContext(),
                R.layout.item_spinner,
                R.id.text_spinner,
                teachersList);
        teachersSpinner.setAdapter(teacherAdapter);

        //Milestone spinner
        milestoneSpinner.setPrompt("Milestone");
        ArrayList<Milestones> milestonesList = new ArrayList<>();
        if (new NewDataParser().getS2mConfiguration() != null) {
            milestonesList = new NewDataParser().getS2mConfiguration().getMilestonesArrayList();
        }
        MilestonesSpinnerAdapter milestoneAdpter = new MilestonesSpinnerAdapter(getContext(),
                R.layout.item_spinner,
                R.id.text_spinner,
                milestonesList);
        milestoneSpinner.setAdapter(milestoneAdpter);

        setSpinnerPopupHeight();
    }

    void setSpinnerPopupHeight() {

        //Reduce spinner drop down view height
        {
            android.util.TypedValue value = new android.util.TypedValue();
            getActivity().getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, value, true);
            android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float ret = value.getDimension(metrics);
            teachersSpinner.setMinimumHeight((int) (ret - 1 * metrics.density));
            milestoneSpinner.setMinimumHeight((int) (ret - 1 * metrics.density));
        }
        // Set popupWindow height

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(teachersSpinner);
            android.widget.ListPopupWindow popupWindow1 = (android.widget.ListPopupWindow) popup.get(milestoneSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(Utils.getInstance().getPixelAsDp(getContext(), 200));
            popupWindow1.setHeight(Utils.getInstance().getPixelAsDp(getContext(), 200));
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }

    }

    VolleyStringRequest sectionAddRequest;

    void addSection(final String className, final String sectionName, final String milestoneId, final String userId, final String studCOunt) {
        sectionAddRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR +
                KEY_SECTIONS + SEPERATOR + KEY_ASSIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.getInstance().showToast("Section added successfully");
                        Log.d("sectionAddRequest", "response: " + response);
                        closeButton.performClick();
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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(KEY_CLASS, className);
                params.put(KEY_SECTION, sectionName);
                params.put(KEY_MILESTONE_ID, milestoneId);
                params.put(KEY_USER_ID, userId);
                params.put(KEY_STUDENT_COUNT, studCOunt);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(sectionAddRequest);
    }

    void updateSections(final int sectionId, final String className, final String sectionName, final String milestoneId, final String userId, final String studCOunt) {

        sectionAddRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL + SharedPreferenceHelper.getSchoolId()
                + SEPERATOR + KEY_SECTIONS + SEPERATOR + sectionId + SEPERATOR + KEY_ASSIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("sectionUpdate", "onResponse: " + response);
                        Toast.makeText(getActivity(), "Section added successfully", Toast.LENGTH_SHORT).show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        //   ((TeachersSectionsFragment) pagerAdapter.getItem(0)).loadTeachers();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("sectionUpdate", "onErrorResponse: " + error);

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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(KEY_CLASS, className);
                params.put(KEY_SECTION, sectionName);
                params.put(KEY_MILESTONE_ID, milestoneId);
                params.put(KEY_USER_ID, userId);
                params.put(KEY_STUDENT_COUNT, studCOunt);
                return params;
            }


        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(sectionAddRequest);
    }

}

