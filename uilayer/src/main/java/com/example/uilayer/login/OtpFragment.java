package com.example.uilayer.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
    @BindView(R.id.button_login_otp)
    ImageButton buttonOtpOk;
    @BindView(R.id.edit_text_otp)
    EditText edtTextOtp;
    @BindView(R.id.request_otp)
    TextView requstOtpText;
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
        return view;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onOtpEntered();
        }
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
                if (textViewTextChangeEvent.text().length() > 0) {
                    edtTextOtp.setCursorVisible(true);
                    edtTextOtp.setSelection(textViewTextChangeEvent.text().length());

                } else edtTextOtp.setCursorVisible(true);
                enteredOtp = textViewTextChangeEvent.text().toString();
            }
        }));
        buttonOtpOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateOtp(enteredOtp))
                    onButtonPressed();
                else
                    edtTextOtp.setError("Please Enter Valid OTP");
            }
        });
        requstOtpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Send OTP again", Toast.LENGTH_SHORT).show();
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
