package com.example.thoughtchimp.s2mconnect.tindercard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.thoughtchimp.s2mconnect.R;

/**
 * Created by thoughtchimp on 11/10/2016.
 */

public class TouchViewActivity extends AppCompatActivity implements View.OnTouchListener {


        private ImageView mImageView;
        private ViewGroup mRrootLayout;
        private int _xDelta;
        private int _yDelta;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mRrootLayout = (ViewGroup) findViewById(R.id.root);
            mImageView = (ImageView) mRrootLayout.findViewById(R.id.imageView);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
            mImageView.setLayoutParams(layoutParams);
            mImageView.setOnTouchListener(this);
        }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = X-250;
                layoutParams.bottomMargin = Y-250;
                view.setLayoutParams(layoutParams);
                break;
        }
        mRrootLayout.invalidate();
        return true;
    }

}
