package com.wowconnect.ui.landing;


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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wowconnect.NewDataHolder;
import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.UserAccessController;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.ui.customUtils.views.HeightWrapListView;
import com.wowconnect.ui.manage.ManageTeachersActivity;
import com.wowconnect.ui.manage.SelectSchoolActivity;
import com.wowconnect.ui.notification.NotificationActivity;
import com.wowconnect.ui.tickets.TicketsFragment;
import com.wowconnect.ui.tickets.attachments.AttachmentsAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
    @BindView(R.id.list_menu)
    HeightWrapListView menuList;
    Animation rotate_forward, rotate_backward;
    boolean isFabsShown;
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
        initAnimations();
    }

    @Override
    protected void onResume() {

        UserAccessController userAccessController = new UserAccessController(SharedPreferenceHelper.getUserId());

        if (userAccessController.hasNavigationMenu()) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(userAccessController.getNavigationMenu());
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        } else {
            toolbar.setNavigationIcon(null);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


        if (getSupportActionBar() != null) {
            String schoolName = NewDataHolder
                    .getInstance(this)
                    .getSchoolById(SharedPreferenceHelper.getSchoolId()).getName();
            getSupportActionBar().setTitle(schoolName != null &&
                    !schoolName.isEmpty() ? schoolName : getResources().getString(R.string.app_name));
        }
        //loading home fragment for first time
        switch (selectedTab)

        {
            case 1:
                homeButton.performClick();
                break;
            case 2:
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
        super.onResume();
    }

    boolean isMenuShown;

    void initAnimations() {
        rotate_forward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate_backward);
    }

    void showMenu() {

        fab.startAnimation(rotate_forward);
        int[] iconIds = {R.drawable.ic_intro_training,
                R.drawable.ic_intro_training,
                R.drawable.ic_intro_training};
        int menuId = 0;

        ArrayList<String> userRoles = new NewDataParser().getUserRoles(this, SharedPreferenceHelper.getUserId());
        if (new DataBaseUtil(this).getUser(SharedPreferenceHelper.getUserId()).getType().equals(Constants.USER_TYPE_S2M_ADMIN))
            menuId = R.array.home_menu_s2m_admin;
        else if (userRoles.contains(Constants.ROLE_COORDINATOR))
            menuId = R.array.home_menu_coordinator;
        else if (userRoles.contains(Constants.ROLE_SCL_ADMIN))
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
        fab.startAnimation(rotate_backward);
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
        //2setScrollableToolbarTitle();


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
                //checkFirebase();
            }
        });
        dummyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuShown)
                    hideMenu();
            }
        });


    }

    void checkFirebase() {
        // String iid = FirebaseInstanceId.getInstance().getId();
        String authorizedEntity = "s2mv2-76810"; // Project id from Google Developer Console
        String scope = "GCM"; // e.g. communicating using GCM, but you can use any
        // URL-safe characters up to a maximum of 1000, or
        // you can also leave it blank.
        try {
            // String token = FirebaseInstanceId.getInstance().getToken();

            //String oldId = FirebaseInstanceId.getInstance().getId();
            // FirebaseInstanceId.getInstance().deleteToken(authorizedEntity, scope);
            FirebaseInstanceId.getInstance().deleteInstanceId();
            // String newIID = FirebaseInstanceId.getInstance().getId();
            //String newToken = FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            Log.d("token", "checkFirebase: " + e.toString());
        }

    }

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
        int id = item.getItemId();
        if (id == R.id.action_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
            //startActivity(new Intent(this, SchoolPagerActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_select_school:
                startActivity(new Intent(LandingActivity.this, SelectSchoolActivity.class));
                break;
            case R.id.nav_manage_teachers:
                startActivity(new Intent(LandingActivity.this, ManageTeachersActivity.class).putExtra("isTeachers", true));
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
