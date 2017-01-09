package com.example.uilayer.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.S2MApplication;
import com.example.uilayer.signup.SignUpActivity;
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
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.edit_text_login)
    EditText loginText;
    @BindView(R.id.prefix_num)
    TextView prefixCountry;
    @BindView(R.id.button_faq)
    Button faqButton;
    @BindView(R.id.button_sign_up)
    Button signUpButton;
    @BindView(R.id.button_terms_of_use)
    Button touButton;
    @BindView(R.id.prefix_layout)
    LinearLayout inputLayout;
    @BindView(R.id.button_login)
    ImageButton buttonLogin;
    CompositeSubscription subscriptions;
    boolean isMail;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {//String param1, String param2
        LoginFragment fragment = new LoginFragment();
        //  Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        // fragment.setArguments(args);
        return fragment;
    }

    public static boolean isValidEmail(CharSequence target) {

        if (target == null) {
            return false;
        } else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    void launchSignUp() {
        startActivity(new Intent(getActivity(), SignUpActivity.class).putExtra("isSignUp", true));
    }

    /* public void sendOTP(final boolean isMail, final String text) {


         VolleyStringRequest loginRequest = new VolleyStringRequest(Request.Method.POST, Constants.LOGIN_URL,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                         Log.d("log", "onResponse: " + response);
                         storeResponse(response);
                         if (mListener != null) {
                             mListener.onEnteredNumber(response);
                         }
                     }
                 },
                 new VolleyStringRequest.VolleyErrListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         super.onErrorResponse(error);
                         Log.d("log", "onErrorResponsssse: " + error);
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
                 if (isMail)
                     params.put(Constants.KEY_EMAIL, text);
                 else
                     params.put(Constants.KEY_MOBILE, text);
                 return params;
             }
         };
         int socketTimeout = 30000;//30 seconds - change to what you want
         RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
         loginRequest.setRetryPolicy(policy);
         VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(loginRequest);

     }*/
    public void sendOTP(final boolean isMail, final String text) {
        if (mListener != null) {
            mListener.onEnteredNumber();        }
    }

    void storeResponse(String response) {
        try {
            JSONObject responseJson = new JSONObject(response);
            DataHolder.getInstance(getActivity()).setLoginResultJson(responseJson);
            Log.d("OTP", "storeResponse: " + responseJson.getString(Constants.KEY_OTP));
        } catch (JSONException ex) {
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Observable<TextViewTextChangeEvent> editTextBind = RxTextView.textChangeEvents(loginText);
        changeToMail();
        subscriptions.add(editTextBind.subscribe(new Action1<TextViewTextChangeEvent>() {
            @Override
            public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                if (textViewTextChangeEvent.text().length() > 0) {
                    loginText.setCursorVisible(true);
                    loginText.setSelection(textViewTextChangeEvent.text().length());
                    if (TextUtils.isDigitsOnly(textViewTextChangeEvent.text())) {
                        isMail = false;
                        changeToPhoneNumber();
                    } else {
                        isMail = true;
                        changeToMail();
                    }
                } else {
                    // loginText.setCursorVisible(false);
                    changeToMail();
                }
            }
        }));

        buttonLogin.setOnClickListener(this);
        //   loginText.setMaxLines(1);

        loginText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d("key enter", "onEditorAction: entered");
                    buttonLogin.performClick();
                    return true;
                }
                return false;
            }
        });
        signUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                validateInput();
                break;
            case R.id.button_sign_up:
                //launchSignUp();
                break;
        }
    }

    void validateInput() {
        String text = loginText.getText().toString();
        if (text.length() > 0)
            if (isMail)
                if (isValidEmail(text))
                    //makeToast(" " + text + " is a valid mail id. You can enter ");
                    sendOTP(true, text);
                else
                    showInputError("Please enter valid mail id");
            else if (isValidNumber(text))
                //makeToast(" " + text + " is a valid phone number. You can enter ");
                sendOTP(false, text);
            else
                showInputError("Please enter valid phone number");
        else
            showInputError("This field cannot be empty");
    }

    boolean isValidNumber(String number) {
        boolean isValidNumber;

        return TextUtils.isDigitsOnly(number) && number.length() == 10;
    }

    void showInputError(String msg) {
        loginText.setError(msg);
    }

    void makeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    void changeToPhoneNumber() {

        if (inputLayout.getVisibility() != View.VISIBLE)
            //animateView(inputLayout, View.VISIBLE);
            inputLayout.setVisibility(View.VISIBLE);
        // loginText.setInputType(InputType.TYPE_CLASS_NUMBER);
        loginText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        loginText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    void changeToMail() {
        loginText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(256)});
        //  loginText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        if (inputLayout.getVisibility() == View.VISIBLE)
            // animateView(inputLayout, View.GONE);
            inputLayout.setVisibility(View.GONE);
        loginText.setGravity(Gravity.CENTER);
    }

    void animateView(final View view, final int visibility) {
        float fromScale, toScale;
        if (visibility == View.GONE) {
            fromScale = 1;
            toScale = 0;
        } else {
            fromScale = 0;
            toScale = 1;
        }
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromScale, toScale, 1f, 1f);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(visibility);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromScale, toScale);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(200);

        view.startAnimation(animationSet);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onEnteredNumber();

    }
}
