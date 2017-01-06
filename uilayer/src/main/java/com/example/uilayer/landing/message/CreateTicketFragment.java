package com.example.uilayer.landing.message;

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
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Category;
import com.example.domainlayer.models.Ticket;
import com.example.domainlayer.models.User;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataHolder;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.adapters.CategorySpinnerAdapter;
import com.example.uilayer.adapters.TeachersSpinnerAdapter;
import com.example.uilayer.customUtils.PromptSpinner;
import com.example.uilayer.customUtils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.VolleyLog.TAG;
import static com.example.domainlayer.Constants.FB_CHILD_TICKET;
import static com.example.domainlayer.Constants.FB_CHILD_TICKET_DETAILS;
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
    final String TAG = "Fetch message";
    @BindView(R.id.spinner_category)
    PromptSpinner categorySpinner;
    @BindView(R.id.spinner_user)
    PromptSpinner userSpinner;
    @BindView(R.id.close_icon)
    ImageButton closeButton;
    @BindView(R.id.button_add_ticket)
    Button createButton;
    @BindView(R.id.text_subject)
    TextInputEditText subjectText;
    boolean isS2m;
    VolleyStringRequest getUsersRequest;
    BottomSheetBehavior bottomSheetBehavior;
    Dialog thisDialog;
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

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_add_ticket, null);
        dialog.setContentView(contentView);
        thisDialog = dialog;
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
                createTicket(subjectText.getText().toString(), "CONTENT");
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
                user.setPhoneNum(userJson.getString(Constants.KEY_PHONE_NUM));
                user.setAvatar(userJson.getString(Constants.KEY_AVATAR));
                teachersList.add(i, user);
            }

            DataHolder.getInstance(getContext()).setTeachersList(teachersList);
            if (getActivity() != null)
                userSpinner.setAdapter(
                        new TeachersSpinnerAdapter(getActivity(),
                                R.layout.item_spinner,
                                R.id.text_spinner,
                                teachersList));
        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }

    void createTicket(String subject, String type) {
        int id = getTicketId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ticket = database.getReference(FB_CHILD_TICKET_DETAILS);
        DatabaseReference tickets = ticket.child(FB_CHILD_TICKET + id);
        Ticket tiketObj = new Ticket();
        tiketObj.setContent("some content");
        tiketObj.setUserName("user name");
        tiketObj.setStatus("open");
        tiketObj.setProfileUrl("this is a profile url");
        tiketObj.setCategory("content");
        tiketObj.setId(3);
        tiketObj.setNumber(4);
        tickets.setValue(tiketObj);
        ticket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                    Ticket value = childsnap.getValue(Ticket.class);
                    Log.d(TAG, "Value is: " + value.getContent());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    int getTicketId() {
        return 3;
    }
}

