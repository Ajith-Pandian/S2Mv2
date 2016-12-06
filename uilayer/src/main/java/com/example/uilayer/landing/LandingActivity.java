package com.example.uilayer.landing;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uilayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView((R.id.fab))
    FloatingActionButton fab;
    @BindView(R.id.button_home)
    ImageButton homeButton;
    @BindView(R.id.button_section)
    ImageButton sectionButton;
    @BindView(R.id.button_messages)
    ImageButton messagesButton;
    @BindView(R.id.button_video)
    ImageButton videoButton;
    Animation show_fab_1, show_fab_2, show_fab_3, hide_fab_1, hide_fab_2, hide_fab_3, rotate_forward, rotate_backward;
    FloatingActionButton fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    boolean isFabsShown;
    @BindView(R.id.layout_frame)
    FrameLayout frameLayout;
    @BindView(R.id.dummy_view)
    View dummyView;
    View.OnClickListener buttonsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (!view.isActivated()) {
                setState(view);
                switch (view.getId()) {
                    case R.id.button_home:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.button_section:
                        replaceFragment(new SectionsFragment());
                        break;
                    case R.id.button_messages: {
                        MessagesFragment messagesFragment = new MessagesFragment();

                        replaceFragment(messagesFragment);
                    }
                    break;
                    case R.id.button_video:
                        MessagesFragment messagesFragment = new MessagesFragment();

                        replaceFragment(messagesFragment);
                        // replaceFragment(new VideoFragment());
                        break;
                }
            }

        }
    };
    FrameLayout containerFabsLayout;
    RelativeLayout fabButtonsLayout;
    View.OnClickListener fabsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s = "";
            switch (view.getId()) {
                case R.id.fab_1:
                    s = "Video call";
                    break;
                case R.id.fab_2:
                    s = "School photos";
                    break;
                case R.id.fab_3:
                    s = "Message";
                    break;
            }
            Toast.makeText(getApplicationContext(), s + " clicked", Toast.LENGTH_SHORT).show();

        }
    };
    float fab1_left = 0.8f, fab1_bottom = .8f, fab2_bottom = 1.0f, fab3_right = .8f, fab3_bottom = .8f;
    boolean isOutSideClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);

        initNavigationDrawer();
        frameLayout.getForeground().setAlpha(0);

        homeButton.setOnClickListener(buttonsClickListener);
        sectionButton.setOnClickListener(buttonsClickListener);
        messagesButton.setOnClickListener(buttonsClickListener);
        videoButton.setOnClickListener(buttonsClickListener);

        //loading home fragment for first time
        homeButton.performClick();
    }

    void replaceFragment(Fragment fragment) {
        FragmentManager fm;
        FragmentTransaction ft;
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.layout_fragment, fragment)
                .commit();
    }

    void setState(View button) {
        homeButton.setActivated(false);
        sectionButton.setActivated(false);
        messagesButton.setActivated(false);
        videoButton.setActivated(false);
        button.setActivated(true);
    }

    void initNavigationDrawer() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFabsShown)
                    showFabs();
                else
                    hideFabs();
            }
        });
        dummyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabsShown)
                    hideFabs();
            }
        });
    }

    void showFabs() {
        initAnimations();
        fab.startAnimation(rotate_forward);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fabLayout1.getLayoutParams();
        layoutParams1.rightMargin += (int) (fabLayout1.getWidth() * fab1_left);
        layoutParams1.bottomMargin += (int) (fabLayout1.getHeight() * fab1_bottom);
        fabLayout1.setLayoutParams(layoutParams1);
        fabLayout1.startAnimation(show_fab_1);
        fab1.setClickable(true);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fabLayout2.getLayoutParams();
        // layoutParams2.rightMargin += (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (fabLayout2.getHeight() * fab2_bottom);
        fabLayout2.setLayoutParams(layoutParams2);
        fabLayout2.startAnimation(show_fab_2);
        fab2.setClickable(true);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fabLayout3.getLayoutParams();
        layoutParams3.leftMargin += (int) (fabLayout3.getWidth() * fab3_right);
        layoutParams3.bottomMargin += (int) (fabLayout3.getHeight() * fab3_bottom);
        fabLayout3.setLayoutParams(layoutParams3);
        fabLayout3.startAnimation(show_fab_3);
        fab3.setClickable(true);
        isFabsShown = true;
        containerFabsLayout.requestFocus();
        frameLayout.getForeground().setAlpha(220);
        dummyView.setVisibility(View.VISIBLE);

    }

    void hideFabs() {
        fab.startAnimation(rotate_backward);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fabLayout1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fabLayout1.getWidth() * fab1_left);
        layoutParams.bottomMargin -= (int) (fabLayout1.getHeight() * fab1_bottom);
        fabLayout1.setLayoutParams(layoutParams);
        fabLayout1.startAnimation(hide_fab_1);
        fab1.setClickable(false);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fabLayout2.getLayoutParams();
        //layoutParams2.rightMargin -= (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (fabLayout2.getHeight() * fab2_bottom);
        fabLayout2.setLayoutParams(layoutParams2);
        fabLayout2.startAnimation(hide_fab_2);
        fab2.setClickable(false);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fabLayout3.getLayoutParams();
        layoutParams3.leftMargin -= (int) (fabLayout3.getWidth() * fab3_right);
        layoutParams3.bottomMargin -= (int) (fabLayout3.getHeight() * fab3_bottom);
        fabLayout3.setLayoutParams(layoutParams3);
        fabLayout3.startAnimation(hide_fab_3);
        fab3.setClickable(false);
        isFabsShown = false;
        frameLayout.getForeground().setAlpha(0);
        dummyView.setVisibility(View.GONE);

    }

    void initAnimations() {
        //show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        //show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        //show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        show_fab_1 = getAnimation(fab1_left, 0f, fab1_bottom, 0f, 0f, 1f);
        show_fab_2 = getAnimation(0f, 0f, fab2_bottom, 0f, 0f, 1f);
        show_fab_3 = getAnimation(-fab3_right, 0f, fab3_bottom, 0f, 0f, 1f);
        //hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        //hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        //hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);
        hide_fab_1 = getAnimation(-fab1_left, 0f, -fab1_bottom, 0f, 1f, 0f);
        hide_fab_2 = getAnimation(0f, 0f, -fab2_bottom, 0f, 1f, 0f);
        hide_fab_3 = getAnimation(fab3_right, 0f, -fab3_bottom, 0f, 1f, 0f);

        rotate_forward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_backward);
        containerFabsLayout = (FrameLayout) findViewById(R.id.fabs_layout);
        fabButtonsLayout = (RelativeLayout) findViewById(R.id.fab_buttons_layout);

        fabLayout1 = (LinearLayout) containerFabsLayout.findViewById(R.id.layout_fab1);
        fabLayout2 = (LinearLayout) containerFabsLayout.findViewById(R.id.layout_fab2);
        fabLayout3 = (LinearLayout) containerFabsLayout.findViewById(R.id.layout_fab3);

        fab1 = (FloatingActionButton) containerFabsLayout.findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) containerFabsLayout.findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) containerFabsLayout.findViewById(R.id.fab_3);

        fab1.setOnClickListener(fabsClickListener);
        fab2.setOnClickListener(fabsClickListener);
        fab3.setOnClickListener(fabsClickListener);

    }

    Animation getAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, float fromAlpha, float toAlpha) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(getTranslateAnim(fromXDelta, toXDelta, fromYDelta, toYDelta));
        animationSet.addAnimation(getAlphaAnimation(fromAlpha, toAlpha));
        animationSet.setFillAfter(true);
        return animationSet;
    }

    TranslateAnimation getTranslateAnim(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        //TranslateAnimation translateAnimation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromXDelta, Animation.RELATIVE_TO_SELF, toXDelta, Animation.RELATIVE_TO_SELF, fromYDelta, Animation.RELATIVE_TO_SELF, toYDelta);

        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(200);
        return translateAnimation;
    }

    AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(200);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        return alphaAnimation;
    }

    @Override
    public void onBackPressed() {

        if (isFabsShown)
            hideFabs();
        else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
