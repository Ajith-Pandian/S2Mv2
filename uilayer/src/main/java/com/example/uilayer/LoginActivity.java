package com.example.uilayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        subscriptions = new CompositeSubscription();

        //Getting view
        Observable<TextViewTextChangeEvent> editTextBind = RxTextView.textChangeEvents(loginText);
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
                    makeToast(" " + text + " is a valid mail id. You can enter ");
                else
                    showInputError("Please enter valid mail id");
            else if (TextUtils.isDigitsOnly(text))
                makeToast(" " + text + " is a valid phone number. You can enter ");
            else
                showInputError("Please enter valid phone number");
        else
            showInputError("This field cannot be empty");
    }


    void showInputError(String msg)
    {
        loginText.setError(msg);
    }

    void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    void changeToPhoneNumber() {
        loginText.setInputType(InputType.TYPE_CLASS_NUMBER);
        loginText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        if (inputLayout.getVisibility() != View.VISIBLE)
            //animateView(inputLayout, View.VISIBLE);
            inputLayout.setVisibility(View.VISIBLE);
    }

    void changeToMail() {
        loginText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        loginText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
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
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
