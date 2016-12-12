package com.example.uilayer.milestones;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.domainlayer.models.milestones.TMileData;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.milestones.fragments.MilesAudioFragment;
import com.example.uilayer.milestones.fragments.MilesImageFragment;
import com.example.uilayer.milestones.fragments.MilesTextFragment;
import com.example.uilayer.milestones.fragments.MilesVideoFragment;
import com.example.uilayer.models.AudioMiles;
import com.example.uilayer.models.ImageMiles;
import com.example.uilayer.models.VideoMiles;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.TYPE_AUDIO;
import static com.example.domainlayer.Constants.TYPE_IMAGE;
import static com.example.domainlayer.Constants.TYPE_TEXT;
import static com.example.domainlayer.Constants.TYPE_VIDEO;

public class MilesActivity extends AppCompatActivity implements MilesTextFragment.OnFragmentInteractionListener
        , MilesVideoFragment.OnFragmentInteractionListener,
        MilesAudioFragment.OnFragmentInteractionListener, MilesImageFragment.OnFragmentInteractionListener {
    @BindView(R.id.miles_fragment_container)
    LinearLayout milesFragmentContainer;
    @BindView(R.id.text_title_mile)
    TextView textTiltle;

    @Override
    public void onImageFragmentInteraction(Uri uri) {

    }

    @Override
    public void onVideoFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAudioFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    ArrayList<TMileData> mileDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miles);
        ButterKnife.bind(this);
        DataHolder holder = DataHolder.getInstance(getApplicationContext());
        String title = holder.getCurrentClass()+" " + holder.getCurrentSection();
        getSupportActionBar().setTitle("Miles");
        getSupportActionBar().setSubtitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textTiltle.setText(holder.getCurrentMileTitle());

   /*   addFragment(1);
        addFragment(1);
        addFragment(2);
        addFragment(3);
        addFragment(4);
        addFragment(5);*/
        addFragments();
    }

    void addFragments() {
        mileDataArrayList = DataHolder.getInstance(getApplicationContext()).getCurrentMileData();
        Fragment fragment = null;
        for (int i = 0; i < mileDataArrayList.size(); i++) {
            TMileData mileData = mileDataArrayList.get(i);
            String type = mileData.getType();
            switch (type) {
                case TYPE_TEXT:
                    fragment = MilesTextFragment.newInstance(mileData.getTitle(),
                            mileData.getBody());
                    break;
                case TYPE_VIDEO:

                    ArrayList<VideoMiles> milesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        milesList.add(new VideoMiles(j, j, mileData.getUrlsList().get(j)));
                    }
                    fragment = MilesVideoFragment.newInstance("VIDEOS", milesList);
                    break;
                case TYPE_AUDIO:
                    fragment = MilesAudioFragment.newInstance("AUDIO", getAudioMiles());
                    break;
                case TYPE_IMAGE:
                    ArrayList<ImageMiles> imageMilesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        imageMilesList.add(new ImageMiles(j, j, mileData.getTitle(), mileData.getUrlsList().get(j)));
                    }
                    fragment = MilesImageFragment.newInstance("IMAGES", imageMilesList);
                    break;

            }
            if (fragment != null)
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(milesFragmentContainer.getId(), fragment)
                        .commit();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void addFragment(int type) {
        Fragment fragment = null;
        String name = "";
        switch (type) {
            case 1:
                name = "TEXT";
                fragment = MilesTextFragment.newInstance("LEARNING OUTCOMES", getResources().getString(R.string.school_msg_two));
                break;
            case 2:
                name = "VIDEOS";
                fragment = MilesVideoFragment.newInstance("VIDEOS", getVideoMiles());
                break;
            case 3:
                name = "VIDEO";
                fragment = MilesVideoFragment.newInstance("VIDEO", getOneMile());
                break;
            case 4:
                name = "AUDIO";
                fragment = MilesAudioFragment.newInstance("AUDIO", getAudioMiles());
                break;
            case 5:
                name = "IMAGES";
                fragment = MilesImageFragment.newInstance("IMAGES", getImageMiles());
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(milesFragmentContainer.getId(), fragment, name)
                .commit();
    }

    ArrayList<VideoMiles> getVideoMiles() {
        ArrayList<VideoMiles> milesList = new ArrayList<>();
        milesList.add(new VideoMiles(0, 0, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(1, 1, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(2, 2, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(3, 3, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(4, 4, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new VideoMiles(5, 5, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        return milesList;
    }

    ArrayList<AudioMiles> getAudioMiles() {
        ArrayList<AudioMiles> milesList = new ArrayList<>();
        milesList.add(new AudioMiles(0, 0, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new AudioMiles(1, 1, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));

        return milesList;
    }


    ArrayList<ImageMiles> getImageMiles() {
        ArrayList<ImageMiles> milesList = new ArrayList<>();
        milesList.add(new ImageMiles(0, 0, "Image one", "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new ImageMiles(1, 1, "Image two", "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new ImageMiles(2, 2, "Image three", "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new ImageMiles(3, 3, "Image four", "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new ImageMiles(4, 4, "Image five", "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new ImageMiles(5, 5, "Image six", "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        return milesList;
    }

    ArrayList<VideoMiles> getOneMile() {
        ArrayList<VideoMiles> milesList = new ArrayList<>();
        milesList.add(new VideoMiles(0, 0, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        return milesList;
    }
}
