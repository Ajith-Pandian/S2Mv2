package com.example.uilayer.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uilayer.R;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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

    public void sendOTP() {
        if (mListener != null) {
            mListener.onEnteredNumber();
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
                    loginText.setCursorVisible(false);
                    changeToMail();
                }
            }
        }));

        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                validateInput();
        }
    }

    void validateInput() {
        String text = loginText.getText().toString();
        if (text.length() > 0)
            if (isMail)
                if (isValidEmail(text))
                    //makeToast(" " + text + " is a valid mail id. You can enter ");
                    sendOTP();
                else
                    showInputError("Please enter valid mail id");
            else if (isValidNumber(text))
                //makeToast(" " + text + " is a valid phone number. You can enter ");
                sendOTP();
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
    }

    void changeToMail() {
        loginText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(256)});
      //  loginText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        if (inputLayout.getVisibility() == View.VISIBLE)
            // animateView(inputLayout, View.GONE);
            inputLayout.setVisibility(View.GONE);
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