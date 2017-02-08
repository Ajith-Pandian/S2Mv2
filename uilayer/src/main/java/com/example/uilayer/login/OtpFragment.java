package com.example.uilayer.login;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.S2MApplication;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtpFragment} interface
 * to handle interaction events.
 * Use the {@link OtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String TAG = "OtpFragment";
    @BindView(R.id.button_login_otp)
    ImageButton buttonOtpOk;
    @BindView(R.id.edit_text_otp)
    EditText edtTextOtp;
    @BindView(R.id.request_otp)
    TextView requestOtpText;
    CompositeSubscription subscriptions;
    String enteredOtp;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OtpListener mListener;

    public OtpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OtpFragment newInstance() {
        OtpFragment fragment = new OtpFragment();
      /*  Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        subscriptions = new CompositeSubscription();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        ButterKnife.bind(this, view);
        edtTextOtp.requestFocus();
        //open keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        return view;
    }

    public void verifyOtp() {
        VolleyStringRequest otpRequest = new VolleyStringRequest(Request.Method.POST, Constants.AUTHENTICATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("otpRequest", "onResponse:otp " + response);
                        storeResponse(response);

                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("otpRequest", "onResponse: " + error);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();
                params.put(Constants.KEY_USER_NAME, NewDataHolder.getInstance(getContext()).getEnteredUserName());
                params.put(Constants.KEY_OTP, Constants.TEMP_OTP);
                params.put(Constants.KEY_DEVICE_TYPE, Constants.TEMP_DEVICE_TYPE);
                params.put(Constants.KEY_DEVICE_TOKEN,
                        Settings.Secure.getString(S2MApplication.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID));

                Log.d(TAG, "getParams: " + params.toString());
                return params;
            }

        };

        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(otpRequest);
    }

    void storeResponse(String response) {
        try {
            JSONObject responseJson = new JSONObject(response);
            //DataHolder.getInstance(getActivity()).saveUserDetails(responseJson);
            //NewDataHolder.getInstance(getActivity()).saveUserInDb(responseJson);
            NewDataHolder.getInstance(getActivity()).setLoginResult(responseJson);
            new NetworkHelper(getContext()).getDashBoardDetails(new NetworkHelper.NetworkListener() {
                @Override
                public void onFinish() {
                    if (mListener != null) {
                        mListener.onOtpEntered();
                    }
                }
            });

        } catch (JSONException ex) {
            Log.e(TAG, "storeResponse: ", ex);
        }
    }


    public void resendOtp() {
        VolleyStringRequest resendOtpRequest = new VolleyStringRequest(Request.Method.POST, Constants.RESEND_OTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("resendOtpRequest", "onResponse:otp " + response);
                        Toast.makeText(getActivity(), "OTP sent again", Toast.LENGTH_SHORT).show();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("resendOtpRequest", "onResponse: " + error);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();
                params.put(Constants.KEY_USER_NAME, NewDataHolder.getInstance(getContext()).getEnteredUserName());
                return params;
            }

        };
        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(resendOtpRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OtpListener) {
            mListener = (OtpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Observable<TextViewTextChangeEvent> editTextBind = RxTextView.textChangeEvents(edtTextOtp);
        subscriptions.add(editTextBind.subscribe(new Action1<TextViewTextChangeEvent>() {
            @Override
            public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                enteredOtp = textViewTextChangeEvent.text().toString();

                int length = textViewTextChangeEvent.text().length();
                if (length == 4) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    verifyOtp();
                } else if (length > 0) {
                    edtTextOtp.setCursorVisible(true);
                    edtTextOtp.setSelection(textViewTextChangeEvent.text().length());
                } else edtTextOtp.setCursorVisible(true);
            }
        }));
        buttonOtpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateOtp(enteredOtp))
                    verifyOtp();
                else
                    edtTextOtp.setError("Please Enter Valid OTP");
            }
        });
        requestOtpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    boolean validateOtp(String otp) {
        return otp.length() == 4;
    }

    public interface OtpListener {
        // TODO: Update argument type and name
        void onOtpEntered();
    }
}
