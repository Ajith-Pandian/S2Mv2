package com.example.uilayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.uilayer.customUtils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MilestonesActivity extends AppCompatActivity {
    @BindView(R.id.circleImageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);
        ButterKnife.bind(this);

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_image);

        imageView.setImageDrawable(Utils.getInstance().getCirclularImage(getApplicationContext(),imageBitmap));
    }
}
