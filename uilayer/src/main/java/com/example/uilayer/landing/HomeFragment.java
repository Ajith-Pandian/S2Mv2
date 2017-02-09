package com.example.uilayer.landing;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.adapters.SchoolActivitiesSwipeAdapter;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.network.NetworkActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.arjsna.swipecardlib.SwipeCardView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.domainlayer.Constants.ACTIVITIES_URL_SUFFIX;
import static com.example.domainlayer.Constants.ACTIVITY_LIKE_URL_SUFFIX;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.ROLE_COORDINATOR;
import static com.example.domainlayer.Constants.ROLE_SCL_ADMIN;
import static com.example.domainlayer.Constants.TEXT_COORDINATOR;
import static com.example.domainlayer.Constants.TEXT_S2M_ADMIN;
import static com.example.domainlayer.Constants.TEXT_SCL_ADMIN;
import static com.example.domainlayer.Constants.TEXT_TEACHER;
import static com.example.domainlayer.Constants.USER_TYPE_S2M_ADMIN;
import static com.example.domainlayer.Constants.USER_TYPE_SCHOOL;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class HomeFragment extends Fragment {
    final String TAG = "HomeFragment";
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.image_bulletin)
    ImageView bulletinImage;
    @BindView(R.id.layout_network)
    LinearLayout layoutNetwork;
    @BindView(R.id.image_btn_share)
    ImageButton buttonShare;
    @BindView(R.id.image_btn_like)
    ImageButton buttonlike;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.swipe_cards)
    SwipeCardView swipeCardView;
    @BindView(R.id.text_see_all)
    TextView seeAllText;
    @BindView(R.id.layout_last_card)
    RelativeLayout lastCardLayout;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            profileImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(getActivity(), bitmap, 20, 1));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    DbUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        ButterKnife.bind(this, view);
        try {
            loadUserData();
        } catch (SQLException ex) {
            Log.e(TAG, "onCreateView: ", ex);
        }

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        buttonlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewDataHolder.getInstance(getContext()).getBulletin() != null) {

                    new NetworkHelper(getContext()).likeActivity(NewDataHolder.getInstance(getContext()).getBulletin().getId(), new NetworkHelper.LikeListener() {
                        @Override
                        public void onLiked() {
                            buttonlike.setColorFilter(getResources().getColor(R.color.colorPrimary));
                        }

                        @Override
                        public void onUnLiked() {
                            buttonlike.setColorFilter(getResources().getColor(android.R.color.white));

                        }
                    });
                } else Utils.getInstance().showToast("No bulletin");
            }
        });

        initSwipeCards();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NewDataHolder.getInstance(getContext()).getBulletin() != null && NewDataHolder.getInstance(getContext()).getBulletin().isLiked())
            buttonlike.setColorFilter(getResources().getColor(R.color.colorPrimary));
        else
            buttonlike.setColorFilter(getResources().getColor(android.R.color.white));
    }

    void initSwipeCards() {
        ArrayList<SclActs> schoolActivitiesList = NewDataHolder.getInstance(getContext()).getSclActList();
        ArrayList<SclActs> swipeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            swipeList.add(schoolActivitiesList.get(i));
        }
        swipeCardView.setAdapter(new SchoolActivitiesSwipeAdapter(getActivity(), swipeList));
        swipeCardView.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(Object dataObject) {

            }

            @Override
            public void onCardExitRight(Object dataObject) {

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d(TAG, "onAdapterAboutToEmpty: " + itemsInAdapter);
                lastCardLayout.setVisibility(itemsInAdapter == 0 ? VISIBLE : GONE);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

            @Override
            public void onCardExitTop(Object dataObject) {

            }

            @Override
            public void onCardExitBottom(Object dataObject) {

            }
        });
        seeAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSchoolDetails();
            }
        });
        lastCardLayout.setVisibility(GONE);
    }

    void loadUserData() throws SQLException {
        user = new DataBaseUtil(getContext()).getUser();
        String nameString = user.getLastName() != null && !user.getLastName().equals("null") ? user.getFirstName() + " " + user.getLastName() : user.getFirstName();
        name.setText(nameString);
        String userType = user.getType();
        if (userType.equals(USER_TYPE_S2M_ADMIN)) {
            designation.setText(TEXT_S2M_ADMIN);
        } else if (userType.equals(USER_TYPE_SCHOOL)) {
            ArrayList<String> roles = SharedPreferenceHelper.getUserRoles();
            if (roles.contains(ROLE_COORDINATOR))
                designation.setText(TEXT_COORDINATOR);
            else if (roles.contains(ROLE_SCL_ADMIN))
                designation.setText(TEXT_SCL_ADMIN);
            else
                designation.setText(TEXT_TEACHER);
        }


        String avatar = user.getAvatar();
        Bitmap placeHolder = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ph_profile);
        profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(getActivity(), placeHolder));
        Picasso.with(getActivity())
                .load(avatar) //http://i164.photobucket.com/albums/u8/hemi1hemi/COLOR/COL9-6.jpg
                .resize(100, 100)
                .into(target);
        if (NewDataHolder.getInstance(getContext()).getBulletin() != null) {

            String image = NewDataHolder.getInstance(getContext()).getBulletin().getMsg();
            // String image = "";

            if (image != null && !image.equals(""))
                Picasso.with(getActivity())
                        .load(image)
                        .placeholder(R.drawable.ph_bulletin)
                        .into(bulletinImage);
        }
        layoutNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NetworkActivity.class));
            }
        });

    }

    void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    public void launchSchoolDetails() {
        Intent intent = new Intent(getActivity(), SchoolDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}