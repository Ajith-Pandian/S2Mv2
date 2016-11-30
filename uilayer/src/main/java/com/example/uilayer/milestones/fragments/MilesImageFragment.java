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

import com.example.uilayer.R;
import com.example.uilayer.adapters.ImageREcyclerTouchListener;
import com.example.uilayer.customUtils.VideoMilesDecoration;
import com.example.uilayer.milestones.adapters.ImageMilesAdapter;
import com.example.uilayer.models.ImageMiles;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MilesImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MilesImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MilesImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "title";
    private static final String IMAGES_LIST = "images_list";

    private String title;
    private ArrayList<ImageMiles> imageList;

    private OnFragmentInteractionListener mListener;

    public MilesImageFragment() {
        // Required empty public constructor
    }

    public static MilesImageFragment newInstance(String title, ArrayList<ImageMiles> imageList) {
        MilesImageFragment fragment = new MilesImageFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(IMAGES_LIST, imageList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            imageList = (ArrayList<ImageMiles>) getArguments().getSerializable(IMAGES_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;

        view = inflater.inflate(R.layout.fragment_miles_images,
                container, false);
        RecyclerView recyclerView;
        ImageMilesAdapter adapter;

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_mile_images);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new VideoMilesDecoration(getActivity(), 5));

        adapter = new ImageMilesAdapter(getActivity(), imageList);

        recyclerView.setAdapter(adapter);
        //Need to correct code here
        recyclerView.addOnItemTouchListener(new ImageREcyclerTouchListener(getActivity(), recyclerView, new ImageMilesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
           //     bundle.putSerializable("images", images);
                bundle.putInt("position", position);

             /*   FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onImageFragmentInteraction(uri);
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
        void onImageFragmentInteraction(Uri uri);
    }
}
