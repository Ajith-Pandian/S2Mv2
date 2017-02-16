package com.wowconnect.ui.milestones.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wowconnect.R;
import com.wowconnect.models.miles.ImageMiles;
import com.wowconnect.ui.customUtils.views.PinchZoomImageView;

import java.util.ArrayList;

/**
 * Created by thoughtchimp on 11/30/2016.
 */

public class ImageSliderDialogFragment extends DialogFragment {
    private String TAG = ImageSliderDialogFragment.class.getSimpleName();
    private ArrayList<ImageMiles> images;
    private ViewPager viewPager;
    MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblTitle;
    private ImageView closeIcon;
    private int selectedPosition = 0;

    static ImageSliderDialogFragment newInstance(ArrayList<ImageMiles> imagesList, int selectedPosition) {
        ImageSliderDialogFragment f = new ImageSliderDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("images_list", imagesList);
        bundle.putInt("position", selectedPosition);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_image_slider_dialog, container, false);


        viewPager = (ViewPager) v.findViewById(R.id.viewpager_images);
        lblTitle = (TextView) v.findViewById(R.id.text_title_full_screen_slider);
        closeIcon = (ImageView) v.findViewById(R.id.close_icon);

        images = (ArrayList<ImageMiles>)
                getArguments()
                        .getSerializable("images_list");

        selectedPosition =
                getArguments().
                        getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        //  Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new

                MyViewPagerAdapter();

        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
//        lblCount.setText((position + 1) + " of " + images.size());

        ImageMiles image = images.get(position);
        lblTitle.setText(image.getTitle());
        // lblDate.setText(image.getTimestamp());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_image_slider, container, false);

            PinchZoomImageView imageViewPreview = (PinchZoomImageView) view.findViewById(R.id.image_preview);

            ImageMiles image = images.get(position);

            Picasso.with(getActivity()).load(image.getUrl()).into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
