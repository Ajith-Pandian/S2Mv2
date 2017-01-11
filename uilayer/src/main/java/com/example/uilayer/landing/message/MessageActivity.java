package com.example.uilayer.landing.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.domainlayer.models.Message;
import com.example.domainlayer.models.Ticket;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.FB_CHILD_CONVERSATIONS;
import static com.example.domainlayer.Constants.FB_CHILD_TICKET;
import static com.example.domainlayer.Constants.FB_CHILD_TICKET_DETAILS;

public class MessageActivity extends AppCompatActivity {
    @BindView(R.id.button_attachments)
    ImageButton attachButton;
    @BindView(R.id.button_send)
    ImageButton sendButton;
    @BindView(R.id.input_message)
    EditText messageInput;
    @BindView(R.id.recycler_message)
    RecyclerView messageRecycler;
    DatabaseReference messageDbReference;
    ValueEventListener msgsAddValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Message> messages = new ArrayList<>();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                Message value = childSnapshot.getValue(Message.class);
                if (value.getSenderId() == com.example.domainlayer.temp.DataHolder.getInstance(MessageActivity.this).getUser().getId())
                    value.setSend(true);
                else
                    value.setSend(false);
                messages.add(value);
            }
            messageRecycler.setAdapter(new MessagesAdapter(MessageActivity.this, messages));
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.w("FB", "Failed to read value.", error.toException());
        }
    };
    String ticketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            ticketId = getIntent().getStringExtra("ticketId");
        }
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messageRecycler.setLayoutManager(layoutManager);
        messageRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, false));
        fetchMessages();
    }

    void fetchMessages() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messageDbReference = database.getReference("firebaseexample")
                .child(String.valueOf(com.example.domainlayer.temp.DataHolder.getInstance(MessageActivity.this).getUser().getSchoolId()))
                .child("convo")
                .child(ticketId);
        messageDbReference.addValueEventListener(msgsAddValueListener);
    }

    @Override
    protected void onDestroy() {
        messageDbReference.removeEventListener(msgsAddValueListener);
        super.onDestroy();
    }
}
