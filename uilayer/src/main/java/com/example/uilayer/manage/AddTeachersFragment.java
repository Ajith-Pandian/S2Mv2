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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Category;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.User;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataHolder;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.customUtils.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.ADD_TEACHERS_URL_SUFFIX;
import static com.example.domainlayer.Constants.CREATE_TICKET_URL;
import static com.example.domainlayer.Constants.GET_TEACHERS_URL_SUFFIX;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CATEGORY;
import static com.example.domainlayer.Constants.KEY_COUNTRY_CODE;
import static com.example.domainlayer.Constants.KEY_COUNTRY_CODES;
import static com.example.domainlayer.Constants.KEY_CREATE;
import static com.example.domainlayer.Constants.KEY_CREATOR_ID;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_EMAIL;
import static com.example.domainlayer.Constants.KEY_FIRST_NAME;
import static com.example.domainlayer.Constants.KEY_LAST_NAME;
import static com.example.domainlayer.Constants.KEY_MOBILE_NO;
import static com.example.domainlayer.Constants.KEY_PHONE_NUM;
import static com.example.domainlayer.Constants.KEY_RECEIVER_ID;
import static com.example.domainlayer.Constants.KEY_ROLES;
import static com.example.domainlayer.Constants.KEY_ROLES_ARRAY;
import static com.example.domainlayer.Constants.KEY_SCHOOL;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SUBJECT;
import static com.example.domainlayer.Constants.KEY_UPDATE;
import static com.example.domainlayer.Constants.KEY_USERS;
import static com.example.domainlayer.Constants.KEY_USER_TYPE;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TYPE_TEACHER;

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
    VolleyStringRequest getUsersRequest;
    BottomSheetBehavior bottomSheetBehavior;
    Dialog thisDialog;
    VolleyStringRequest createTicketRequest;
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

    public static AddTeachersFragment getNewInstance(boolean isUpdate, int position) {
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
            User teacher = NewDataHolder.getInstance(getContext()).getTeachersList().get(position);
            firstName.setText(teacher.getFirstName());
            lastName.setText(teacher.getLastName());
            email.setText(teacher.getEmail());
            phoneNum.setText(teacher.getPhoneNum());
            title.setText("Update Teachers");
            addButton.setText("Update");

        }
        /*categorySpinner.setPrompt("Category");
        categorySpinner.setAdapter(new CategorySpinnerAdapter(getActivity(), R.layout.item_spinner, R.id.text_spinner, getCategories()));

        if (isUpdate) {
            userSpinner.setVisibility(View.VISIBLE);
            userSpinner.setPrompt("User");
            loadTeachers();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        createButton.requestLayout();
        createButton.requestFocus();
*/
    }

   /* String getCategory() {
        return ((Category) categorySpinner.getSelectedItem()).getName();
    }

    int getUserID() {
        return ((User) userSpinner.getSelectedItem()).getId();
    }*/


    VolleyStringRequest teacherAddRequest;

    void addTeacher(final String firstName, final boolean hasLastName, final String lastName, final boolean hasEmail, final String email, final String phoneNum) {

        teacherAddRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL + SharedPreferenceHelper.getSchoolId()
                + SEPERATOR + KEY_USERS + SEPERATOR + KEY_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Teacher added successfully", Toast.LENGTH_SHORT).show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
                params.put(KEY_FIRST_NAME, firstName);

                if (hasLastName)
                    params.put(KEY_LAST_NAME, lastName);

                if (hasEmail)
                    params.put(KEY_EMAIL, email);

                params.put(KEY_COUNTRY_CODE, Constants.COUNTRY_CODE);
                params.put(KEY_MOBILE_NO, phoneNum);
                params.put(KEY_USER_TYPE, KEY_SCHOOL);
                params.put(KEY_ROLES_ARRAY, TYPE_TEACHER);
                return params;
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(teacherAddRequest);
    }

    void updateTeacher(final int teacherId, final String firstName, final boolean hasLastName, final String lastName, final boolean hasEmail, final String email, final String phoneNum) {

        teacherAddRequest = new VolleyStringRequest(Request.Method.POST, SCHOOLS_URL + SharedPreferenceHelper.getSchoolId()
                + SEPERATOR + KEY_USERS + SEPERATOR + teacherId + SEPERATOR + KEY_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Teacher added successfully", Toast.LENGTH_SHORT).show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(KEY_FIRST_NAME, firstName);

                if (hasLastName)
                    params.put(KEY_LAST_NAME, lastName);

                if (hasEmail)
                    params.put(KEY_EMAIL, email);

                params.put(KEY_COUNTRY_CODE, Constants.COUNTRY_CODE);
                params.put(KEY_PHONE_NUM, phoneNum);
                params.put(KEY_USER_TYPE, TYPE_TEACHER);
                params.put(KEY_ROLES + "[]", "s2m_content");

                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(teacherAddRequest);
    }

}

