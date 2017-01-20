package com.example.uilayer.landing;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataHolder;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.S2MApplication;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.network.NetworkActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.ACTIVITIES_URL_SUFFIX;
import static com.example.domainlayer.Constants.ACTIVITY_LIKE_URL_SUFFIX;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;


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

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            profileImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(getContext(), bitmap, 20, 1));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        ButterKnife.bind(this, view);
        try {
            loadUserData();
        } catch (SQLException ex) {
            Log.e(TAG, "onCreateView: ", ex);
        }
        // Bitmap imageBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.profile);

        //Utils.getInstance().getCirclularImage(getActivity(), imageBitmap);
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
                likeBulletin();
            }
        });
        if((com.example.uilayer.DataHolder.getInstance(getContext()).getUser()
                .getBulletin()
                .getLiked())==1)
                buttonlike.setColorFilter(getResources().getColor(R.color.colorPrimary));
            else
                buttonlike.setColorFilter(getResources().getColor(android.R.color.white));
        return view;
    }

    void loadUserData() throws SQLException {
        user = new DataBaseUtil(S2MApplication.getAppContext()).getUser();
        String nameString = user.getFirstName() + " " + user.getLastName();
        name.setText(nameString);
        // textWow.setText(user.getWow() + Constants.SUFFIX_WOWS);
        // textMiles.setText(user.getMiles() +Constants.SUFFIX_MILES);
        String avatar = user.getAvatar();
        Bitmap placeHolder = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ph_profile);
        profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(getActivity(), placeHolder));
        Picasso.with(getActivity())
                .load(avatar) //http://i164.photobucket.com/albums/u8/hemi1hemi/COLOR/COL9-6.jpg
                .resize(100, 100)
                .into(target);

        Picasso.with(getActivity())
                .load(DataHolder.getInstance(getActivity()).getUser().getBulletin().getMsg().getImage())
                .placeholder(R.drawable.ph_bulletin)
                .into(bulletinImage);
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

    void likeBulletin() {
        VolleyStringRequest likeRequest = new VolleyStringRequest(Request.Method.POST, Constants.SCHOOLS_URL
                + String.valueOf(user.getSchoolId())
                + ACTIVITIES_URL_SUFFIX
                + String.valueOf(com.example.uilayer.DataHolder.getInstance(getContext())
                                                                                .getUser()
                                                                                .getBulletin()
                                                                                .getId())
                + ACTIVITY_LIKE_URL_SUFFIX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("LikeRequest", "onResponse: " + response);
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            String msg = responseJson.getString(KEY_MESSAGE);

                            if (msg.equals(Constants.LIKED))
                                buttonlike.setColorFilter(getResources().getColor(R.color.colorPrimary));
                            else if (msg.equals(Constants.UNLIKED))
                                buttonlike.setColorFilter(getResources().getColor(android.R.color.white));
                            showToast(msg);
                        } catch (JSONException e) {
                        }
                        //  showToast("Liked");
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("LikeRequest-error", "onResponse: " + error);
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
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(likeRequest);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}