package com.example.uilayer.milestones;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.uilayer.R;
import com.example.uilayer.milestones.fragments.MilesTextFragment;
import com.example.uilayer.milestones.fragments.MilesVideoFragment;
import com.example.uilayer.models.VideoMiles;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MilesActivity extends AppCompatActivity implements MilesTextFragment.OnFragmentInteractionListener, MilesVideoFragment.OnFragmentInteractionListener {
    @BindView(R.id.miles_fragment_container)
    LinearLayout milesFragmentContainer;

    @Override
    public void onVideoFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miles);
        ButterKnife.bind(this);
        addFragment(1);
        addFragment(2);
        addFragment(3);
        addFragment(2);
        addFragment(1);
    }

    void addFragment(int type) {
        Fragment fragment = null;
        switch (type) {
            case 1:
                fragment = MilesTextFragment.newInstance("LEARNING OUTCOMES", getResources().getString(R.string.school_msg_two));
                break;
            case 2:
                fragment = MilesVideoFragment.newInstance("VIDEOS", getMile());
                break;
            case 3:
                fragment = MilesVideoFragment.newInstance("VIDEO", getOneMile());
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(milesFragmentContainer.getId(), fragment, null)
                .commit();
    }

    ArrayList<VideoMiles> getMile() {
        ArrayList<VideoMiles> milesList = new ArrayList<>();
        milesList.add(new VideoMiles(0, 0, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(1, 1, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(2, 2, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(3, 3, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(4, 4, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(5, 5, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        return milesList;
    }

    ArrayList<VideoMiles> getOneMile() {
        ArrayList<VideoMiles> milesList = new ArrayList<>();
        milesList.add(new VideoMiles(0, 0, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        return milesList;
    }
}
