package com.example.uilayer.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.domainlayer.models.Notification;
import com.example.uilayer.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity {
    @BindView(R.id.notification_recycler)
    RecyclerView notificationRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationRecycler.setLayoutManager(layoutManager);
        notificationRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_two), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_two), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_two), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_two), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_two), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_two), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_two), "20.01.2017", "SclAct"));
        notifications.add(new Notification("This is First Title", getResources().getString(R.string.school_msg_one), "20.01.2017", "SclAct"));
        notificationRecycler.setAdapter(new NotificationAdapter(this, notifications));
    }
}
