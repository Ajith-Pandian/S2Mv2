package com.example.uilayer.milestones.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uilayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MilesTextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MilesTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MilesTextFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    @BindView(R.id.text_title_fragment_mile_text)
    TextView textTitle;
    @BindView(R.id.text_content_fragment_mile_text)
    TextView textContent;
    // TODO: Rename and change types of parameters
    private String title;
    private String content;
    private OnFragmentInteractionListener mListener;

    public MilesTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title   Parameter 1.
     * @param content Parameter 2.
     * @return A new instance of fragment MilesTextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MilesTextFragment newInstance(String title, String content) {
        MilesTextFragment fragment = new MilesTextFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            content = getArguments().getString(CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_miles_text, container, false);
        ButterKnife.bind(this, view);
        textTitle.setText(title);
        content = "<p>Lorem Ipsum is simply dummy text" +
                " of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
                "It has survived not only five centuries, but also the leap into electronic typesetting," +
                " remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset" +
                " sheets containing Lorem Ipsum passages.</p><br><p>Lorem Ipsum is simply dummy text of the printing " +
                "and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s," +
                " but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the " +
                "1960s with the release of Letraset sheets containing Lorem Ipsum passages,</p>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textContent.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textContent.setText(Html.fromHtml(content));
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
