package com.example.thoughtchimp.s2mconnect;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thoughtchimp.s2mconnect.horizontalScrollCards.BlurBuilder;
import com.example.thoughtchimp.s2mconnect.horizontalScrollCards.HorizontalRecyclerAdapter;
import com.example.thoughtchimp.s2mconnect.horizontalScrollCards.HorizontalSpaceItemDecoration;
import com.example.thoughtchimp.s2mconnect.horizontalScrollCards.Miles;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class MessagesFragment extends Fragment {
    public static final int SINGLE = 0, MULTIPLE = 1;
    public static final String KEY_TYPE = "fragment_type";
    List<Miles> milesList = new ArrayList<>();
    int type = MULTIPLE;

    public MessagesFragment() {

    }

    @TargetApi(17)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.type = bundle.getInt(KEY_TYPE);
        }

        if (type == SINGLE) {
            milesList = getOneMile();
            view = inflater.inflate(R.layout.fragment_messages_single,
                    container, false);
            final ImageView imageView = (ImageView) view.findViewById(R.id.singleMileImageView);
            final ImageView backImage = (ImageView) view.findViewById(R.id.backgroundImage);
            backImage.setVisibility(View.INVISIBLE);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImageBitmap(bitmap);

                    Bitmap blurredBitmap = BlurBuilder.blur(getActivity(), bitmap);
                    BitmapDrawable drawableBitmap = new BitmapDrawable(getResources(), blurredBitmap);
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        backImage.setBackgroundDrawable(drawableBitmap);
                    } else {
                        backImage.setBackground(drawableBitmap);
                    }
                    backImage.setVisibility(View.VISIBLE);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            Picasso.with(getActivity()).load(milesList.get(0).getUrl()).into(target);
        } else {
            milesList = getMiles();
            view = inflater.inflate(R.layout.fragment_messages_multi,
                    container, false);
            RecyclerView recyclerView;
            HorizontalRecyclerAdapter adapter;

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 5));

            adapter = new HorizontalRecyclerAdapter(getActivity(), milesList);

            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    List<Miles> getMiles() {
        List<Miles> milesList = new ArrayList<>();
        milesList.add(new Miles(0, 0, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new Miles(1, 1, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new Miles(2, 2, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new Miles(3, 3, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new Miles(4, 4, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        milesList.add(new Miles(5, 5, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        return milesList;
    }

    List<Miles> getOneMile() {
        List<Miles> milesList = new ArrayList<>();
        milesList.add(new Miles(0, 0, "http://weknowyourdreams.com/images/nature/nature-02.jpg"));
        return milesList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}