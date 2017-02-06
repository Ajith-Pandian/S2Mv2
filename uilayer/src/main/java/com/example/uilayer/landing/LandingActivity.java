package com.example.uilayer.landing;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.DbUser;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.S2MApplication;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.views.HeightWrapListView;
import com.example.uilayer.tickets.TicketsFragment;
import com.example.uilayer.manage.ManageTeachersActivity;
import com.example.uilayer.manage.SelectSchoolActivity;
import com.example.uilayer.notification.NotificationActivity;
import com.example.uilayer.tickets.attachments.AttachmentsAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.ROLE_COORDINATOR;
import static com.example.domainlayer.Constants.ROLE_SCL_ADMIN;
import static com.example.domainlayer.Constants.TYPE_S2M_ADMIN;

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
    @BindView(R.id.list_menu)
    HeightWrapListView menuList;
    /*Animation show_fab_1, show_fab_2, show_fab_3, hide_fab_1, hide_fab_2, hide_fab_3, rotate_forward, rotate_backward;
    FloatingActionButton fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    boolean isFabsShown;*/
    @BindView(R.id.layout_frame)
    FrameLayout frameLayout;
    @BindView(R.id.dummy_view)
    View dummyView;
    int selectedTab = 0;
    View.OnClickListener buttonsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (!view.isActivated()) {
                setState(view);
                switch (view.getId()) {
                    case R.id.button_home:
                        selectedTab = 1;
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.button_section:
                        selectedTab = 2;
                        replaceFragment(new SectionsFragment());
                        break;
                    case R.id.button_messages: {
                        selectedTab = 3;
                        TicketsFragment ticketsFragment = new TicketsFragment();
                        replaceFragment(ticketsFragment);
                    }
                    break;
                    case R.id.button_video:
                        selectedTab = 4;
                        TicketsFragment ticketsFragment = new TicketsFragment();
                        replaceFragment(ticketsFragment);
                        break;
                }
            }

        }
    };
    /* FrameLayout containerFabsLayout;
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
                     s = "BulletinMessage";
                     break;
             }
             Toast.makeText(getApplicationContext(), s + " clicked", Toast.LENGTH_SHORT).show();

         }
     };*/
    //float fab1_left = 0.8f, fab1_bottom = .8f, fab2_bottom = 1.0f, fab3_right = .8f, fab3_bottom = .8f;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);

        initNavigationDrawer();
        frameLayout.getForeground().setAlpha(0);

        // setupWindowAnimations();
        homeButton.setOnClickListener(buttonsClickListener);
        sectionButton.setOnClickListener(buttonsClickListener);
        messagesButton.setOnClickListener(buttonsClickListener);
        videoButton.setOnClickListener(buttonsClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(SharedPreferenceHelper.getSchoolName() != null &&
                    !SharedPreferenceHelper.getSchoolName().equals("") ? SharedPreferenceHelper.getSchoolName() : getResources().getString(R.string.app_name));
        }
        //loading home fragment for first time
        switch (selectedTab) {
            case 0:
                homeButton.performClick();
                break;
            case 1:
                sectionButton.performClick();
                break;
            case 3:
                messagesButton.performClick();
                break;
            case 4:
                videoButton.performClick();
                break;
            default:
                homeButton.performClick();
        }

    }

    boolean isMenuShown;

    void showMenu() {
        int[] iconIds = {R.drawable.ic_intro_training,
                R.drawable.ic_intro_training,
                R.drawable.ic_intro_training};
        int menuId = 0;

        ArrayList<String> userRoles = SharedPreferenceHelper.getUserRoles();
        if (new DataBaseUtil(this).getUser().getType().equals(TYPE_S2M_ADMIN))
            menuId = R.array.home_menu_s2m_admin;
        else if (userRoles.contains(ROLE_COORDINATOR))
            menuId = R.array.home_menu_coordinator;
        else if (userRoles.contains(ROLE_SCL_ADMIN))
            menuId = R.array.home_menu_school_admin;
        else
            menuId = R.array.home_menu_teacher;

        //TODO:More roles validation
        AttachmentsAdapter adapter =
                new AttachmentsAdapter(this, getResources().getStringArray(menuId), iconIds);
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        giveFeedback();
                        break;
                    case 1:
                        sharePicture();
                        break;
                    case 2:
                        callAdmin();
                        break;
                }
            }
        });
        menuList.setVisibility(View.VISIBLE);
        frameLayout.getForeground().setAlpha(220);
        dummyView.setVisibility(View.VISIBLE);
        isMenuShown = true;
    }

    void hideMenu() {
        menuList.setVisibility(View.GONE);
        dummyView.setVisibility(View.GONE);
        frameLayout.getForeground().setAlpha(0);
        menuList.setAdapter(null);
        menuList.setOnItemClickListener(null);
        isMenuShown = false;
    }

    void giveFeedback() {
    }

    void sharePicture() {
    }

    void callAdmin() {
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

    void setScrollableToolbarTitle() {
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView toolbarTextView = (TextView) f.get(toolbar);
            toolbarTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            toolbarTextView.setFocusable(true);
            toolbarTextView.setFocusableInTouchMode(true);
            toolbarTextView.requestFocus();
            toolbarTextView.setSingleLine(true);
            toolbarTextView.setSelected(true);
            toolbarTextView.setMarqueeRepeatLimit(-1);
            // set text on Textview
            // toolbarTextView.setText("Hello Android ! This is a sample marquee text. That's great. Enjoy");
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }

    void initNavigationDrawer() {
        setSupportActionBar(toolbar);
        setScrollableToolbarTitle();


        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMenuShown)
                    showMenu();
                else
                    hideMenu();
            }
        });
        dummyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuShown)
                    hideMenu();
            }
        });
        String userType = new DataBaseUtil(this).getUser().getType();
        switch (userType) {
            case Constants.TYPE_TEACHER:
                toolbar.setNavigationIcon(null);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case Constants.TYPE_SCL_ADMIN:
            case Constants.TYPE_T_SCL_ADMIN:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_landing_drawer_school_admin);
                break;
            case Constants.TYPE_S2M_ADMIN:
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_landing_drawer_s2m_admin);
                break;
        }

    }

/*    void showFabs() {
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
    }*/

    @Override
    public void onBackPressed() {

        if (isMenuShown)
            hideMenu();
        else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
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
            startActivity(new Intent(this, NotificationActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_select_school:
                startActivity(new Intent(LandingActivity.this, SelectSchoolActivity.class));
                break;
            case R.id.nav_manage_teachers:
                startActivity(new Intent(LandingActivity.this, ManageTeachersActivity.class).putExtra("isTeachers", true));
                break;
            case R.id.nav_manage_sections:
                startActivity(new Intent(LandingActivity.this, ManageTeachersActivity.class).putExtra("isTeachers", false));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toggle != null)
            drawer.addDrawerListener(toggle);
    }
}
