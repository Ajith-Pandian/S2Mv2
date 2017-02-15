package com.wowconnect.ui.manage;

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
import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.DbUser;
import com.wowconnect.models.Milestones;
import com.wowconnect.models.Sections;
import com.wowconnect.ui.adapters.MilestonesSpinnerAdapter;
import com.wowconnect.ui.adapters.TeachersSpinnerAdapter;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.ui.customUtils.views.PromptSpinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    BottomSheetBehavior bottomSheetBehavior;
    Dialog thisDialog;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_DRAGGING:
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    break;
                case BottomSheetBehavior.STATE_HIDDEN:
                    dismiss();
                    break;
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
    static AddOrUpdateListener addOrUpdateListener;

    public static AddSectionsFragment getNewInstance(boolean isUpdate, int position, AddOrUpdateListener listener) {
        addOrUpdateListener = listener;
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
            if (getDialog() != null && getDialog().getWindow() != null) {
                final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                getDialog().getWindow().setAttributes(params);
            }
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
        }
    }

    String getTextFromView(EditText editText) {
        return editText.getText().toString();
    }

    int getSelectedSectionId() {
        return NewDataHolder.getInstance(getContext()).getSectionsList().get(this.position).getId();
    }

    String getSpinnerTeacherId() {
        if (teachersSpinner.getSelectedItem() != null)
            return String.valueOf(((DbUser) teachersSpinner.getSelectedItem()).getId());
        else {
            return "";
        }
    }

    String getSpinnerMilestoneId() {
        if (milestoneSpinner.getSelectedItem() != null)
            return String.valueOf(((Milestones) milestoneSpinner.getSelectedItem()).getId());
        else {
            return "";
        }
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
                validateAndAddSection();
            }
        });

        if (isUpdate) {
            currentSection = NewDataHolder.getInstance(getContext()).getSectionsList().get(position);
            className.setText(currentSection.get_Class());
            sectionName.setText(currentSection.getSection());
            selectMilestone(currentSection.getMilestoneId());
            selectTeacher(currentSection.getTeacherId());
            numOfStudents.setText(String.valueOf(currentSection.getNumOfStuds()));
            addButton.setText("Update");
            title.setText("Update Sections");
        }
    }

    Sections currentSection;

    private void validateAndAddSection() {
        Utils utils = Utils.getInstance();
        if (getTextFromView(className).isEmpty())
            utils.showToast("Please enter class name");
        else if (getTextFromView(sectionName).isEmpty())
            utils.showToast("Please enter section name");
        else if (getSpinnerMilestoneId().equals(""))
            utils.showToast("Please select milestone");
        else if (getTextFromView(numOfStudents).isEmpty())
            utils.showToast("Please enter number of students");
        else if (isUpdate && Integer.parseInt(getTextFromView(numOfStudents).trim()) < currentSection.getNumOfStuds())
            utils.showToast("Cannot reduce student count. Please increase it");
        else {
            String classNameString = getTextFromView(className);
            String sectionNameString = getTextFromView(sectionName);
            String numOfStudsString = getTextFromView(numOfStudents);


            if (isUpdate) {
                updateSections(getSelectedSectionId(),
                        classNameString,
                        sectionNameString,
                        String.valueOf(getSpinnerMilestoneId()),
                        String.valueOf(getSpinnerTeacherId()),
                        numOfStudsString);
            } else {
                addSection(classNameString,
                        sectionNameString,
                        String.valueOf(getSpinnerMilestoneId()),
                        String.valueOf(getSpinnerTeacherId()),
                        numOfStudsString);
            }
        }
    }

    void selectMilestone(int milestoneId) {
        ArrayList<Milestones> milestonesList = new NewDataParser().getS2mConfiguration().getMilestonesArrayList();

        for (int i = 0; i < milestonesList.size(); i++) {
            if (milestoneId == milestonesList.get(i).getId())
                milestoneSpinner.setSelection(i);
        }
    }

    NetworkHelper networkHelper;

    void selectTeacher(final int teacherId) {
        ArrayList<DbUser> teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();
        if (teachersList != null && teachersList.size() > 0) {
            for (int i = 0; i < teachersList.size(); i++) {
                if (teacherId == teachersList.get(i).getId())
                    teachersSpinner.setSelection(i);
            }
        } else {
            networkHelper = new NetworkHelper(getContext());
            networkHelper.getNetworkUsers(new NetworkHelper.NetworkListener() {
                @Override
                public void onFinish() {
                    ArrayList<DbUser> teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();
                    if (teachersList != null && teachersList.size() > 0) {
                        for (int i = 0; i < teachersList.size(); i++) {
                            if (teacherId == teachersList.get(i).getId())
                                teachersSpinner.setSelection(i);
                        }
                    }
                }
            });
        }
    }


    ArrayList<DbUser> teachersList = new ArrayList<>();

    void initSpinners() {
        //Teacher spinner
        teachersSpinner.setPrompt("Teacher");
        if (NewDataHolder.getInstance(getContext()).getTeachersList() != null) {
            teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();
        } else {
            new NetworkHelper(getContext()).getNetworkUsers(new NetworkHelper.NetworkListener() {
                @Override
                public void onFinish() {
                    teachersList = NewDataHolder.getInstance(getContext()).getTeachersList();
                }
            });
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
            if (teachersSpinner.getAdapter() != null && teachersSpinner.getAdapter().getCount() > 5)
                popupWindow.setHeight(Utils.getInstance().getPixelAsDp(getContext(), 200));

            if (milestoneSpinner.getAdapter() != null && milestoneSpinner.getAdapter().getCount() > 5)
                popupWindow1.setHeight(Utils.getInstance().getPixelAsDp(getContext(), 200));
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            Log.d(TAG, "setSpinnerPopupHeight: " + e.toString());
        }

    }

    private static final String TAG = "AddSectionsFragment";
    VolleyStringRequest sectionAddRequest;

    void addSection(final String className, final String sectionName, final String milestoneId, final String teacherId, final String studCOunt) {
        sectionAddRequest = new VolleyStringRequest(Request.Method.POST, Constants.SCHOOLS_URL +
                SharedPreferenceHelper.getSchoolId() + Constants.SEPERATOR +
                Constants.KEY_SECTIONS + Constants.SEPERATOR + Constants.KEY_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.getInstance().showToast("Section added successfully");
                        Log.d("sectionAddRequest", "response: " + response);
                        addOrUpdateListener.onFinish(false);
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
                params.put(Constants.KEY_CLASS, className);
                params.put(Constants.KEY_SECTION, sectionName);
                params.put(Constants.KEY_MILESTONE_ID, milestoneId);
                if (!studCOunt.isEmpty())
                    params.put(Constants.KEY_STUDENT_COUNT, studCOunt);
                else
                    params.put(Constants.KEY_STUDENT_COUNT, "0");
                if (!teacherId.isEmpty())
                    params.put(Constants.KEY_TEACHER_ID, teacherId);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(sectionAddRequest);
    }

    void updateSections(final int sectionId, final String className, final String sectionName, final String milestoneId, final String userId, final String studCOunt) {

        sectionAddRequest = new VolleyStringRequest(Request.Method.POST, Constants.SCHOOLS_URL + SharedPreferenceHelper.getSchoolId()
                + Constants.SEPERATOR + Constants.KEY_SECTIONS + Constants.SEPERATOR + sectionId + Constants.SEPERATOR + Constants.KEY_ASSIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("sectionUpdate", "onResponse: " + response);
                        Toast.makeText(getActivity(), "Section Updated successfully", Toast.LENGTH_SHORT).show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        //   ((TeachersSectionsFragment) pagerAdapter.getItem(0)).getTeachers();

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
                params.put(Constants.KEY_CLASS, className);
                params.put(Constants.KEY_SECTION, sectionName);
                params.put(Constants.KEY_MILESTONE_ID, milestoneId);
                params.put(Constants.KEY_TEACHER_ID, userId);
                params.put(Constants.KEY_STUDENT_COUNT, studCOunt);
                Log.d("Update Sections", "getParams: " + params.toString());
                return params;
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(sectionAddRequest);
    }

    @Override
    public void onDestroy() {
        if (sectionAddRequest != null) {
            sectionAddRequest.removeStatusListener();
        }
        if (networkHelper != null) {
            networkHelper.removeNetworkListener();
        }
        super.onDestroy();
    }
}

