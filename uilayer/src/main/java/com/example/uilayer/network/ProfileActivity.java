package com.example.uilayer.network;

import android.graphics.drawable.ClipDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.domainlayer.models.User;

import com.example.uilayer.R;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_MILES;
import static com.example.domainlayer.Constants.KEY_TRAINING;
import static com.example.domainlayer.Constants.KEY_WOW;
import static com.example.domainlayer.Constants.SPACE;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_profile)
    Toolbar toolbar;
    @BindView(R.id.image_profile_toolbar)
    ImageView profileImage;
    @BindView(R.id.tabs_profile)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_profile)
    ViewPager viewPager;
    @BindView(R.id.text_wows)
    TextView wowsText;
    @BindView(R.id.text_miles)
    TextView milesText;
    @BindView(R.id.text_trainings)
    TextView trainingsText;

    ProfilePagerAdapter profilePagerAdapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("com.example.domainlayer.models.User");
        toolbar.setTitle(user.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(profilePagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        wowsText.setText(user.getWow() + SPACE + KEY_WOW);
        milesText.setText(user.getMiles() + SPACE + KEY_MILES);
        trainingsText.setText(user.getTrainings() + SPACE + KEY_TRAINING);

        Picasso.with(getApplicationContext()).load(user.getAvatar()).into(profileImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class ProfilePagerAdapter extends FragmentPagerAdapter {

        public ProfilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            boolean type;
            switch (position) {
                case 0:
                    type = true;
                    break;
                case 1:
                    type = false;
                    break;
                default:
                    type = true;
                    break;

            }
            return ProfileFragment.newInstance(type, user);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BASIC";
                case 1:
                    return "SECTIONS";
            }
            return null;
        }
    }

}