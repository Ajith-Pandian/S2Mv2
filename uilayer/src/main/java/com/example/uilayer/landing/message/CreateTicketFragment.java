package com.example.uilayer.landing.message;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Category;
import com.example.domainlayer.models.User;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataHolder;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.adapters.CategorySpinnerAdapter;
import com.example.uilayer.adapters.TeachersSpinnerAdapter;
import com.example.uilayer.customUtils.PromptSpinner;
import com.example.uilayer.customUtils.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.GET_TEACHERS_URL_SUFFIX;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN1;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

/**
 * Created by thoughtchimp on 1/4/2017.
 */

public class CreateTicketFragment extends BottomSheetDialogFragment {
    private static final String IS_S2M = "isS2m";
    static CreateTicketFragment newInstance;
    @BindView(R.id.spinner_category)
    PromptSpinner categorySpinner;
    @BindView(R.id.spinner_user)
    PromptSpinner userSpinner;
    @BindView(R.id.close_icon)
    ImageButton closeButton;
    @BindView(R.id.button_add_ticket)
    Button createButton;
    boolean isS2m;
    VolleyStringRequest getUsersRequest;
    BottomSheetBehavior bottomSheetBehavior;
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

    public static CreateTicketFragment getNewInstance(boolean isS2m) {
        newInstance = new CreateTicketFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_S2M, isS2m);
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
            isS2m = getArguments().getBoolean(IS_S2M);
        }

    }
Dialog thisDialog;
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_add_ticket, null);
        dialog.setContentView(contentView);
        thisDialog=dialog;
        ButterKnife.bind(this, contentView);
        initViews();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = ((BottomSheetBehavior) behavior);
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
            bottomSheetBehavior.setPeekHeight(Utils.getInstance().getPixelAsDp(dialog.getContext(), 300));
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

    void initViews() {

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                thisDialog.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getBaseContext(), "Ticket Added", Toast.LENGTH_SHORT).show();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                thisDialog.dismiss();
            }
        });
        categorySpinner.setPrompt("Category");
        categorySpinner.setAdapter(new CategorySpinnerAdapter(getActivity(), R.layout.item_spinner, R.id.text_spinner, getCategories()));

        if (isS2m) {
            userSpinner.setVisibility(View.VISIBLE);
            userSpinner.setPrompt("User");
            loadTeachers();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        createButton.requestLayout();
        createButton.requestFocus();

    }

    ArrayList<Category> getCategories() {
        ArrayList<Category> categoryArrayList = new ArrayList<>();

        categoryArrayList.add(new Category(1, "Content"));
        categoryArrayList.add(new Category(1, "Accounts"));
        categoryArrayList.add(new Category(1, "Training"));
        categoryArrayList.add(new Category(1, "Assessments"));
        return categoryArrayList;
    }

    public void loadTeachers() {
        getUsersRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL + "2" + GET_TEACHERS_URL_SUFFIX,
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
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN1);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(getUsersRequest);

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
                // user.setType(userJson.getString(Constants.KEY_TYPE));
                teachersList.add(i, user);
            }

            DataHolder.getInstance(getContext()).setTeachersList(teachersList);
            userSpinner.setAdapter(new TeachersSpinnerAdapter(getActivity(), R.layout.item_spinner, R.id.text_spinner, teachersList));
        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }
}
