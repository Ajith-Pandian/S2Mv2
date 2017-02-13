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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.wowconnect.NewDataHolder;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.DbUser;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wowconnect.domain.Constants.SCHOOLS_URL;


/**
 * Created by thoughtchimp on 1/4/2017.
 */

public class AddTeachersFragment extends BottomSheetDialogFragment {
    private static final String IS_UPDATE = "isUpdate";
    private static final String POSITION = "position";
    static AddTeachersFragment newInstance;
    final String TAG = "Fetch message";
    @BindView(R.id.text_create_teacher)
    TextView title;
    @BindView(R.id.text_teacher_first_name)
    TextInputEditText firstName;
    @BindView(R.id.text_teacher_last_name)
    TextInputEditText lastName;
    @BindView(R.id.text_mobile_number)
    TextInputEditText phoneNum;
    @BindView(R.id.text_teacher_email)
    TextInputEditText email;
    @BindView(R.id.close_icon)
    ImageButton closeButton;
    @BindView(R.id.button_add_teacher)
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

    public static AddTeachersFragment getNewInstance(boolean isUpdate, int position, AddOrUpdateListener listener) {
        addOrUpdateListener = listener;
        newInstance = new AddTeachersFragment();
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
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_add_teacher, null);
        dialog.setContentView(contentView);
        thisDialog = dialog;
        ButterKnife.bind(this, contentView);
        initViews();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = ((BottomSheetBehavior) behavior);
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
            bottomSheetBehavior.setPeekHeight(Utils.getInstance().getPixelAsDp(dialog.getContext(), 350));
        }
    }

    String getTextFromView(EditText editText) {
        return editText.getText().toString();
    }

    int getSelectedTeacherId() {
        return NewDataHolder.getInstance(getContext()).getTeachersList().get(this.position).getId();
    }

    void initViews() {

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
                boolean hasLastName, hasEmail;
                hasLastName = !getTextFromView(lastName).equals("");
                hasEmail = !getTextFromView(email).equals("");
                if (isUpdate) {
                    updateTeacher(getSelectedTeacherId(),
                            getTextFromView(firstName),
                            hasLastName, getTextFromView(lastName),
                            hasEmail, getTextFromView(email),
                            getTextFromView(phoneNum));
                } else {
                    addTeacher(getTextFromView(firstName),
                            hasLastName, getTextFromView(lastName),
                            hasEmail, getTextFromView(email),
                            getTextFromView(phoneNum));
                }
            }
        });

        if (isUpdate) {
            DbUser teacher = NewDataHolder.getInstance(getContext()).getTeachersList().get(position);
            firstName.setText(teacher.getFirstName());
            lastName.setText(teacher.getLastName());
            email.setText(teacher.getEmail());
            phoneNum.setText(teacher.getPhoneNum());
            title.setText("Update Teachers");
            addButton.setText("Update");

        }
    }


    void addTeacher(final String firstName,
                    final boolean hasLastName, final String lastName,
                    final boolean hasEmail, final String email, final String phoneNum) {

        teachersUpdateRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL + SharedPreferenceHelper.getSchoolId()
                + Constants.SEPERATOR +Constants. KEY_USERS + Constants.SEPERATOR + Constants.KEY_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Teacher added successfully", Toast.LENGTH_SHORT).show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        addOrUpdateListener.onFinish(false);
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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(Constants.KEY_FIRST_NAME, firstName);

                if (hasLastName)
                    params.put(Constants.KEY_LAST_NAME, lastName);

                if (hasEmail)
                    params.put(Constants.KEY_EMAIL, email);

                params.put(Constants.KEY_COUNTRY_CODE, Constants.COUNTRY_CODE);
                params.put(Constants.KEY_MOBILE_NO, phoneNum);
                params.put(Constants.KEY_USER_TYPE, Constants.KEY_SCHOOL);
                params.put(Constants.KEY_ROLES_ARRAY, Constants.ROLE_TEACHER);
                return params;
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(teachersUpdateRequest);
    }

    VolleyStringRequest teachersUpdateRequest;

    void updateTeacher(final int teacherId, final String firstName, final boolean hasLastName, final String lastName, final boolean hasEmail, final String email, final String phoneNum) {

        teachersUpdateRequest = new VolleyStringRequest(Request.Method.POST, Constants.SCHOOLS_URL + SharedPreferenceHelper.getSchoolId()
                + Constants.SEPERATOR + Constants.KEY_USERS + Constants.SEPERATOR + teacherId + Constants.SEPERATOR + Constants.KEY_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Teacher updated successfully", Toast.LENGTH_SHORT).show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        addOrUpdateListener.onFinish(true);
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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(Constants.KEY_FIRST_NAME, firstName);

                if (hasLastName)
                    params.put(Constants.KEY_LAST_NAME, lastName);

                if (hasEmail)
                    params.put(Constants.KEY_EMAIL, email);

                params.put(Constants.KEY_COUNTRY_CODE, Constants.COUNTRY_CODE);
                params.put(Constants.KEY_PHONE_NUM, phoneNum);

                return params;
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(teachersUpdateRequest);
    }

}

