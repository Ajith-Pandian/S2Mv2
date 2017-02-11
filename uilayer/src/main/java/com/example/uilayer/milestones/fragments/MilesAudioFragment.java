package com.example.uilayer.milestones.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.VideoMilesDecoration;
import com.example.uilayer.milestones.adapters.AudioMilesAdapter;
import com.example.uilayer.models.AudioMiles;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MilesAudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MilesAudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MilesAudioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String VIDEOS_LIST = "videos_list";

    private String title;
    private ArrayList<AudioMiles> audiList;

    private OnFragmentInteractionListener mListener;

    public MilesAudioFragment() {
        // Required empty public constructor
    }

    public static MilesAudioFragment newInstance(String title, ArrayList<AudioMiles> audioList) {
        MilesAudioFragment fragment = new MilesAudioFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(VIDEOS_LIST, audioList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            audiList = (ArrayList<AudioMiles>) getArguments().getSerializable(VIDEOS_LIST);
        }
        // audiList=getMile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_miles_audio,
                container, false);
        RecyclerView recyclerView;
        TextView titleText;
        AudioMilesAdapter adapter;

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        titleText = (TextView) view.findViewById(R.id.text_title_fragment_mile_text);

        if (!title.isEmpty())
            titleText.setText(title);
        else
            titleText.setText("Audio");
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new VideoMilesDecoration(getActivity(), 8));

        adapter = new AudioMilesAdapter(getActivity(), audiList);

        recyclerView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAudioFragmentInteraction(uri);
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
        void onAudioFragmentInteraction(Uri uri);
    }
}
