package com.example.uilayer.message;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Message;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.CREATE_MSG_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CONTENT;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_INTRO_CONTENT;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_TICKET_ID;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.NETWORK_SECTIONS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN1;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

public class MessageActivity extends AppCompatActivity {
    @BindView(R.id.button_attachments)
    ImageButton attachButton;
    @BindView(R.id.button_send)
    ImageButton sendButton;
    @BindView(R.id.input_message)
    EditText messageInput;
    @BindView(R.id.recycler_message)
    RecyclerView messageRecycler;
    @BindView(R.id.toolbar_message)
    Toolbar toolbar;

    DatabaseReference messageDbReference;
    ValueEventListener msgsAddValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Message> messages = new ArrayList<>();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                Message value = childSnapshot.getValue(Message.class);
                int userId = com.example.domainlayer.temp.DataHolder.
                        getInstance(MessageActivity.this).getUser().getId();
                if (value.getSenderId() == userId)
                    value.setSend(true);
                else
                    value.setSend(false);
                //value.setType("text");
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
    /*  @BindView(R.id.button_back_toolbar)
        ImageButton backButton;
        @BindView(R.id.button_close_toolbar)
        ImageButton closeTicketButton;*/
    VolleyStringRequest sendMessageRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        // ButterKnife.bind(this, toolbar);
        if (getIntent() != null) {
            ticketId = getIntent().getStringExtra("ticketId");
        }


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(layoutManager);
        messageRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, false));
        fetchMessages();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageInput.getText().toString();
                if (msg.length() > 0)
                    sendMessage(msg, "text");
            }
        });

    }

    void sendMessage(final String message, final String type) {
        sendMessageRequest = new VolleyStringRequest(Request.Method.POST, CREATE_MSG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendMessageRequest", "onResponse: " + response);

                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("sendMessageRequest", "onErrorResponse: " + error);

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


            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(KEY_TICKET_ID, ticketId);
                params.put(KEY_CONTENT, message);
                params.put(KEY_TYPE, type);
                params.put(KEY_SCHOOL_ID, String.valueOf(com.example.domainlayer.temp.DataHolder.
                        getInstance(MessageActivity.this).getUser().getSchoolId()));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(sendMessageRequest);
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
        if (sendMessageRequest != null)
            sendMessageRequest.removeStatusListener();
        super.onDestroy();
    }
}
