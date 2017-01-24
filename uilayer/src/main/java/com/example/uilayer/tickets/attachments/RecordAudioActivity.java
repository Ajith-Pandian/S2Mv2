package com.example.uilayer.tickets.attachments;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.views.AudioVisualizer;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordAudioActivity extends AppCompatActivity {

    public static final int REPEAT_INTERVAL = 40;
    MediaRecorder recorder;
    boolean isRecording;
    Handler visulaUpdateHandler;
    @BindView(R.id.visualizer)
    AudioVisualizer visualizer;
    @BindView(R.id.button_record)
    FloatingActionButton recordButton;


    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isRecording) // if we are already recording
            {
                // get the current amplitude
                int x = recorder.getMaxAmplitude();
                visualizer.addAmplitude(x); // update the VisualizeView
                visualizer.invalidate(); // refresh the visualizer

                // update in 40 milliseconds
                visulaUpdateHandler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };
    @BindView(R.id.text_timer)
    TextView timerTextView;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    String audioPath;
    private long startHTime = 0L;
    private Handler timerHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            if (timerTextView != null)
                timerTextView.setText("" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
            timerHandler.postDelayed(this, 0);
        }

    };
    View.OnClickListener recordClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!isRecording) {
                // isRecording = true;

                recordButton.setImageDrawable(ContextCompat.getDrawable(RecordAudioActivity.this, R.drawable.ic_stop_black_24dp));
                recorder = new MediaRecorder();

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(audioPath);

                MediaRecorder.OnErrorListener errorListener = null;
                recorder.setOnErrorListener(errorListener);
                MediaRecorder.OnInfoListener infoListener = null;
                recorder.setOnInfoListener(infoListener);

                try {
                    recorder.prepare();
                    recorder.start();
                    isRecording = true; // we are currently recording
                    startHTime = SystemClock.uptimeMillis();
                    timerHandler.postDelayed(updateTimerThread, 0);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                visulaUpdateHandler.post(updateVisualizer);

            } else {
                recordButton.setImageDrawable(ContextCompat.getDrawable(RecordAudioActivity.this, R.drawable.ic_mic_black_24dp));
                releaseRecorder();
                timerTextView.setText("00:00");
                setResult(RESULT_OK, getIntent());
                finish();
            }

        }
    };


    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String[] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        ButterKnife.bind(this);
        visulaUpdateHandler = new Handler();
        recordButton.setOnClickListener(recordClickListener);
        if (getIntent() != null)
            audioPath = getIntent().getStringExtra("path");

        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) RecordAudioActivity.super.finish();
        if (!permissionToWriteAccepted) RecordAudioActivity.super.finish();

    }

    private void releaseRecorder() {
        if (recorder != null) {
            isRecording = false; // stop recording
            visulaUpdateHandler.removeCallbacks(updateVisualizer);
            visualizer.clear();
            recorder.stop();
            timeSwapBuff += timeInMilliseconds;
            timerHandler.removeCallbacks(updateTimerThread);
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }
}
