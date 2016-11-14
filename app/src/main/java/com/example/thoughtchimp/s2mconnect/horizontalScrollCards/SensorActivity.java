package com.example.thoughtchimp.s2mconnect.horizontalScrollCards;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.thoughtchimp.s2mconnect.R;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView textView;
  //  private ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //declaring Sensor Manager and sensor type
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //locate views
        textView = (TextView) findViewById(R.id.txt);
       // image = (ImageView) findViewById(R.id.img);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if (Math.abs(x) > Math.abs(y)) {
            if (x < 0) {
               // image.setImageResource(R.drawable.right);
                textView.setText("You tilt the device right");
            }
            if (x > 0) {
               // image.setImageResource(R.drawable.left);
                textView.setText("You tilt the device left");
            }
        } else {
            if (y < 0) {
               // image.setImageResource(R.drawable.up);
                textView.setText("You tilt the device up");
            }
            if (y > 0) {
               // image.setImageResource(R.drawable.down);
                textView.setText("You tilt the device down");
            }
        }
        if (x > (-2) && x < (2) && y > (-2) && y < (2)) {
           // image.setImageResource(R.drawable.center);
            textView.setText("Not tilt device");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
    }
}

