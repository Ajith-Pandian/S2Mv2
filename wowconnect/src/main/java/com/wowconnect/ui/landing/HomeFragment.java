package com.wowconnect.ui.landing;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.UserAccessController;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.models.DbUser;
import com.wowconnect.models.SclActs;
import com.wowconnect.ui.adapters.SchoolActivitiesSwipeAdapter;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.network.NetworkActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.arjsna.swipecardlib.SwipeCardView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


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

    Target profileTarget = new Target() {
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
                if (NewDataHolder.getInstance(getContext()).getBulletin() != null
                        && bulletinBitmap != null) {
                    // shareBulletin();
                    shareBitmap(bulletinBitmap, "Bulletin Board");
                } else Utils.getInstance().showToast("No bulletin");
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
                            NewDataHolder.getInstance(getContext()).getBulletin().setLiked(true);
                        }

                        @Override
                        public void onUnLiked() {
                            buttonlike.setColorFilter(getResources().getColor(android.R.color.white));
                            NewDataHolder.getInstance(getContext()).getBulletin().setLiked(false);
                        }
                    });
                } else Utils.getInstance().showToast("No bulletin");
            }
        });

        return view;
    }

    Target bulletinTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bulletinBitmap = bitmap;
            bulletinImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    Bitmap bulletinBitmap;

    private void shareBitmap(Bitmap bitmap, String fileName) {
        try {
            File file = new File(getContext().getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            /*file.setReadable(true, false);*/
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/*");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

 /*   private void shareBulletin() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "WoW Connect Bulletin");
        shareIntent.putExtra(Intent.EXTRA_STREAM, bulletinBitmap);
        shareIntent.setType("image*//*");
        startActivity();
    }*/

    @Override
    public void onResume() {
        super.onResume();
        if (NewDataHolder.getInstance(getContext()).getBulletin() != null && NewDataHolder.getInstance(getContext()).getBulletin().isLiked()) {
            buttonlike.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else
            buttonlike.setColorFilter(getResources().getColor(android.R.color.white));
        initSwipeCards();

    }

    void initSwipeCards() {
        ArrayList<SclActs> schoolActivitiesList = NewDataHolder.getInstance(getContext()).getSclActList();
        ArrayList<SclActs> swipeList = new ArrayList<>();
        if (schoolActivitiesList != null && schoolActivitiesList.size() > 0) {
            Collections.reverse(schoolActivitiesList);
            seeAllText.setText("see all");
            lastCardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchSchoolDetails();
                }
            });

            int cardsCount = schoolActivitiesList.size() < 5 ? schoolActivitiesList.size() : 5;
            for (int i = 0; i < cardsCount; i++) {
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
                    boolean visibility = itemsInAdapter == 0;
                    lastCardLayout.setVisibility(visibility ? VISIBLE : GONE);
                    swipeCardView.setVisibility(!visibility ? VISIBLE : GONE);
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
        } else {
            lastCardLayout.setVisibility(VISIBLE);
            swipeCardView.setVisibility(GONE);
            seeAllText.setText("No Activities");
            lastCardLayout.setOnClickListener(null);
        }
    }

    void loadUserData() throws SQLException {
        user = new DataBaseUtil(getContext()).getUser(SharedPreferenceHelper.getUserId());
        String nameString = user.getLastName() != null && !user.getLastName().equals("null") ? user.getFirstName() + " " + user.getLastName() : user.getFirstName();
        name.setText(nameString);
        UserAccessController accessController = new UserAccessController(user.getId());
        designation.setText(accessController.getDesignation());

        String avatar = user.getAvatar();
        Bitmap placeHolder = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ph_profile);
        profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(getActivity(), placeHolder));
        Picasso.with(getActivity())
                .load(avatar) //http://i164.photobucket.com/albums/u8/hemi1hemi/COLOR/COL9-6.jpg
                .resize(100, 100)
                .into(profileTarget);
        if (NewDataHolder.getInstance(getContext()).getBulletin() != null) {

            String image = NewDataHolder.getInstance(getContext()).getBulletin().getMsg();
            // String image = "";

            if (image != null && !image.isEmpty())
                Picasso.with(getActivity())
                        .load(image)
                        .placeholder(R.drawable.ph_bulletin_board)
                        .into(bulletinTarget);
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