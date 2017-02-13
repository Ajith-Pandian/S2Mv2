package com.wowconnect.ui.milestones.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowconnect.R;
import com.wowconnect.ui.customUtils.VideoMilesDecoration;
import com.wowconnect.ui.milestones.adapters.VideoMilesAdapter;
import com.wowconnect.models.miles.VideoMiles;
import com.google.android.youtube.player.YouTubeIntents;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MilesVideoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MilesVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MilesVideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String VIDEOS_LIST = "videos_list";

    private String title;
    private ArrayList<VideoMiles> videoList;

    private OnFragmentInteractionListener mListener;

    public MilesVideoFragment() {
        // Required empty public constructor
    }

    public static MilesVideoFragment newInstance(String title, ArrayList<VideoMiles> videoList) {
        MilesVideoFragment fragment = new MilesVideoFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(VIDEOS_LIST, videoList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            videoList = (ArrayList<VideoMiles>) getArguments().getSerializable(VIDEOS_LIST);
        }
        // videoList=getMile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (videoList.size() == 1) {
            view = inflater.inflate(R.layout.fragment_miles_video_single,
                    container, false);
            final ImageView imageView = (ImageView) view.findViewById(R.id.singleMileImageView);
            TextView titleText = (TextView) view.findViewById(R.id.text_title_fragment_mile_text);
            ImageButton playButton = (ImageButton) view.findViewById(R.id.button_play);


            if (!title.isEmpty())
                titleText.setText(title);
            else
                titleText.setText("Video");
            playButton.setOnClickListener(playClickListener);
            imageView.setOnClickListener(playClickListener);
            String url = "https://img.youtube.com/vi/" + videoList.get(0).getVideoId() + "/0.jpg";
            Picasso.with(getActivity())
                    .load(url)
                    .placeholder(R.drawable.ph_video_large)
                    .into(imageView);
        } else {

            view = inflater.inflate(R.layout.fragment_miles_video_multi,
                    container, false);
            RecyclerView recyclerView;
            VideoMilesAdapter adapter;

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            TextView titleText;

            titleText = (TextView) view.findViewById(R.id.text_title_fragment_mile_text);

            if (!title.isEmpty())
                titleText.setText(title);
            else
                titleText.setText("Videos");

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.addItemDecoration(new VideoMilesDecoration(getActivity(), 8));

            adapter = new VideoMilesAdapter(getActivity(), videoList);

            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onVideoFragmentInteraction(uri);
        }
    }

    View.OnClickListener playClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String DEVELOPER_KEY = "AIzaSyD9lGPATA3aXGnl0GWMICk1Y883TArl30Y";
            startActivity(YouTubeIntents.createPlayVideoIntentWithOptions(getActivity(),
                    videoList.get(0).getVideoId(), true, true));
        }
    };

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
        void onVideoFragmentInteraction(Uri uri);
    }
}
