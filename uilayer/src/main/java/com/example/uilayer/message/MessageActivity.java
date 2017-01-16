package com.example.uilayer.message;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static com.example.domainlayer.Constants.CREATE_MSG_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CONTENT;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_TICKET_ID;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

public class MessageActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 3;
    static final int REQUEST_TAKE_PHOTO = 1;
    final int REQUEST_AUDIO_RECORDING = 2;
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
    ImageButton backButton;
    Button closeTicketButton;
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
    String mCurrentPhotoPath;

    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        // ButterKnife.bind(this, toolbar);
        if (getIntent() != null) {
            ticketId = getIntent().getStringExtra("ticketId");
        }
        backButton = (ImageButton) toolbar.findViewById(R.id.button_back_toolbar);
        closeTicketButton = (Button) toolbar.findViewById(R.id.button_close_toolbar);

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
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(attachButton);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

    private void showPopupMenu(View view) {
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_attach, popup.getMenu());
        popup.setOnMenuItemClickListener(new AttachmentMenuClickListener(0));
        popup.show();
    }

    @Override
    protected void onDestroy() {
        messageDbReference.removeEventListener(msgsAddValueListener);
        if (sendMessageRequest != null)
            sendMessageRequest.removeStatusListener();
        super.onDestroy();
    }

    private void dispatchAudioIntent() {
        Intent intent =
                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (isAvailable(getApplicationContext(), intent) && intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,
                    REQUEST_AUDIO_RECORDING);
            Log.d("Audio", "started: ");
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (isAvailable(getApplicationContext(), takeVideoIntent) && takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            Log.d("Video", "started: ");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.uilayer.fileProvider",
                        photoFile);
              //  takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d("image", "started: " + photoURI.toString());

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // File storageDir = Environment.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,
                resultCode, intent);
        if (requestCode == REQUEST_AUDIO_RECORDING && resultCode == RESULT_OK) {
            Uri audioUri = intent.getData();
            Log.d("Audio", "onActivityResult: " + audioUri.toString());
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            Log.d("Video", "onActivityResult: " + videoUri.toString());
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && intent.getData() != null) {
            Uri imageUri = intent.getData();
            Log.d("image", "onActivityResult: " + imageUri.toString());
           /*     Bundle extras = intent.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");*/
            //mImageView.setImageBitmap(imageBitmap);
        } else {
            super.onActivityResult(requestCode,
                    resultCode, intent);
        }


    }

    public class AttachmentMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public AttachmentMenuClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_image:
                    dispatchTakePictureIntent();
                    return true;
                case R.id.menu_audio:
                    dispatchAudioIntent();
                    return true;
                case R.id.menu_video:
                    dispatchTakeVideoIntent();
                    return true;
                case R.id.menu_document:
                    return true;
            }
            return false;
        }
    }
}
