package com.example.wowconnect.ui.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wowconnect.NewDataHolder;
import com.example.wowconnect.NewDataParser;
import com.example.wowconnect.R;
import com.example.wowconnect.SharedPreferenceHelper;
import com.example.wowconnect.domain.Constants;
import com.example.wowconnect.models.DbUser;
import com.example.wowconnect.ui.profile.ProfileUpdateActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NetworkProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_profile)
    Toolbar toolbar;
    @BindView(R.id.image_profile_toolbar)
    ImageView profileImage;
    @BindView(R.id.tabs_profile)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_profile)
    ViewPager viewPager;
    @BindView(R.id.layout_teacher_details)
    LinearLayout teacherDetailsLayout;
    @BindView(R.id.text_wows)
    TextView wowsText;
    @BindView(R.id.text_miles)
    TextView milesText;
    @BindView(R.id.text_trainings)
    TextView trainingsText;

    ProfilePagerAdapter profilePagerAdapter;
    DbUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        //user = (User) bundle.getSerializable("com.example.wowconnect.domain.models.User");
        user = NewDataHolder.getInstance(this).getCurrentNetworkUser();
        toolbar.setTitle(user.getFirstName() + " " + user.getLastName());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (profilePagerAdapter.getCount() == 1) {
            //tabLayout.setSelectedTabIndicatorHeight(0);
            tabLayout.setVisibility(View.GONE);
        }

        if (new NewDataParser()
                .getUserRoles(getApplicationContext(), user.getId())
                .contains(Constants.ROLE_TEACHER))
            teacherDetailsLayout.setVisibility(View.GONE);

        wowsText.setText(user.getWow() + Constants.SPACE + Constants.KEY_WOW);
        milesText.setText(user.getMiles() + Constants.SPACE + Constants.KEY_MILES);
        trainingsText.setText(user.getTrainings() + Constants.SPACE + Constants.KEY_TRAINING);
        if (!user.getAvatar().equals(""))
            Picasso.with(getApplicationContext()).load(user.getAvatar()).into(profileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user.getId() == SharedPreferenceHelper.getUserId()) {
            getMenuInflater().inflate(R.menu.menu_profile, menu);
            return true;
        } else
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_edit_profile:
                startActivity(new Intent(this, ProfileUpdateActivity.class));
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
            type = position == 0;
            return ProfileFragment.newInstance(type);
        }

        @Override
        public int getCount() {
            return getValidTabsCount();
        }

        int getValidTabsCount() {
            if (new NewDataParser()
                    .getUserRoles(getApplicationContext(), user.getId())
                    .contains(Constants.ROLE_TEACHER))
                return 2;
            else return 1;

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

