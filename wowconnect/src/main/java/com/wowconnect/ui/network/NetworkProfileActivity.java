package com.wowconnect.ui.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.wowconnect.NewDataHolder;
import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.S2MApplication;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.UserAccessController;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.DbUser;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.ui.profile.ProfileUpdateActivity;

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
    @BindView(R.id.layout_wow)
    LinearLayout wowLayout;

    ProfilePagerAdapter profilePagerAdapter;
    DbUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        user = NewDataHolder.getInstance(this).getCurrentNetworkUser();
        toolbar.setTitle(user.getFirstName() + " " + user.getLastName());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (new UserAccessController(SharedPreferenceHelper.getUserId()).isS2mAdmin()) {
            wowLayout.setClickable(true);
            wowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giveWow();
                }
            });
        }
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (profilePagerAdapter.getCount() == 1) {
            tabLayout.setVisibility(View.GONE);
        }

        if (!new NewDataParser()
                .getUserRoles(getApplicationContext(), user.getId())
                .contains(Constants.ROLE_TEACHER))
            teacherDetailsLayout.setVisibility(View.GONE);

        wowsText.setText(String.valueOf(user.getWow()) + Constants.SPACE + Constants.KEY_WOWS);
        milesText.setText(user.getMiles() + Constants.SPACE + Constants.KEY_MILES);
        trainingsText.setText(user.getTrainings() + Constants.SPACE + Constants.KEY_TRAINING);
        if (!user.getAvatar().equals(""))
            Picasso.with(getApplicationContext()).load(user.getAvatar()).into(profileImage);
    }


    private void giveWow() {
        VolleyStringRequest giveWowRequest = new VolleyStringRequest(Request.Method.POST,
                Constants.USERS_URL + user.getId() + Constants.SEPERATOR +
                        Constants.KEY_WOWS + Constants.SEPERATOR + Constants.KEY_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("giveWowRequest", "onResponse " + response);
                        user.setWow(user.getWow() + 1);
                        new DataBaseUtil(NetworkProfileActivity.this).updateUser(user);
                        wowsText.setText(String.valueOf(user.getWow()) + Constants.SPACE + Constants.KEY_WOWS);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("giveWowRequest", "onResponse: " + error);
                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
                //Utils.getInstance().showToast("try later");
                Utils.getInstance().showToast(getResources().getString(R.string.er_wow_given));
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });
        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(giveWowRequest);
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
            return new UserAccessController(user.getId()).hasProfileSections() ? 2 : 1;
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

