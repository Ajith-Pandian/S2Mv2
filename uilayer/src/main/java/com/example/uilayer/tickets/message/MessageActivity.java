package com.example.uilayer.tickets.message;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

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
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Message;
import com.example.domainlayer.network.VolleyMultipartRequest;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.views.HeightWrapListView;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;
import com.example.uilayer.tickets.attachments.AddDocumentActivity;
import com.example.uilayer.tickets.attachments.AttachmentsAdapter;
import com.example.uilayer.tickets.attachments.RecordAudioActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.AUDIO_RECORD_CODE;
import static com.example.domainlayer.Constants.CREATE_MSG_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CONTENT;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_NEW_STATUS;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_TICKET_ID;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.MANAGE_TICKET_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TYPE_AUDIO;
import static com.example.domainlayer.Constants.TYPE_IMAGE;
import static com.example.domainlayer.Constants.TYPE_VIDEO;

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
    @BindView(R.id.list_attachments)
    HeightWrapListView attachmentsList;
    @BindView(R.id.layout_bottom_message)
    RelativeLayout messageInputLayout;
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
                //value.setUserType("text");
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
    String creatorId;
    /*  @BindView(R.id.button_back_toolbar)
        ImageButton backButton;
        @BindView(R.id.button_close_toolbar)
        ImageButton closeTicketButton;*/
    VolleyStringRequest sendMessageRequest;

    String mCurrentFilePath;
    String uploadFilePath = "";
    String uploadFileMime = "";
    String uploadFileExtension = "";
    VolleyStringRequest closeTicketRequest;
    boolean isAttachmentShown = false;

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
            creatorId = getIntent().getStringExtra("creatorId");
        }
        backButton = (ImageButton) toolbar.findViewById(R.id.button_back_toolbar);
        closeTicketButton = (Button) toolbar.findViewById(R.id.button_close_toolbar);

        if (creatorId.equals(String.valueOf(com.example.domainlayer.temp.DataHolder.
                getInstance(MessageActivity.this).getUser().getId())))
            closeTicketButton.setVisibility(View.VISIBLE);
        else
            closeTicketButton.setVisibility(View.GONE);

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
                // showPopupMenu(attachButton);
                if (!isAttachmentShown)
                    showAttachmentPopup();
                else
                    hideAttachmentPopup();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        closeTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTicket();
            }
        });
       // ViewCompat.setElevation(messageInputLayout, 12);
       // ViewCompat.setElevation(attachmentsList, 8);
        final ViewGroup viewGroup =  (ViewGroup) this.findViewById(android.R.id.content);
        viewGroup.bringChildToFront(messageInputLayout);
    }

    void showAttachmentPopup() {
        int[] iconIds = {
                R.drawable.ic_intro_training,
                R.drawable.ic_intro_training,
                R.drawable.ic_intro_training,
                R.drawable.ic_intro_training};
        AttachmentsAdapter adapter =
                new AttachmentsAdapter(this, getResources().getStringArray(R.array.attachment_menu), iconIds);
        attachmentsList.setAdapter(adapter);
        attachmentsList.setVisibility(View.VISIBLE);
        isAttachmentShown = true;
/*
       attachmentsList.animate()
                 .translationY(0)
                .alpha(1.0f)
                .setDuration(300)
               .setListener(null).start();
*/
        //slideToTop(attachmentsList);
    }

    void hideAttachmentPopup() {
        attachmentsList.setVisibility(View.GONE);
        isAttachmentShown = false;
       /*attachmentsList.animate()
                .translationY(attachmentsList.getHeight())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        attachmentsList.setVisibility(View.GONE);

                    }
                }).start();*/
        //slideToBottom(attachmentsList);
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(1000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }
    public void slideToBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(1000);
        animate.setFillAfter(false);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
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

    void closeTicket() {
        closeTicketRequest = new VolleyStringRequest(Request.Method.POST, MANAGE_TICKET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("closeTicketRequest", "onResponse: " + response);
                        finish();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("closeTicketRequest", "onErrorResponse: " + error);

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
                params.put(KEY_NEW_STATUS, "closed");
                params.put(KEY_SCHOOL_ID, String.valueOf(com.example.domainlayer.temp.DataHolder.
                        getInstance(MessageActivity.this).getUser().getSchoolId()));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(closeTicketRequest);
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
        Intent recordAudioIntent =
                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

        if (recordAudioIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File audioFile = null;
            try {
                audioFile = createTempFile(TYPE_AUDIO);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            if (isAvailable(getApplicationContext(), recordAudioIntent) && recordAudioIntent.resolveActivity(getPackageManager()) != null) {
                Uri audioUri = FileProvider.getUriForFile(this,
                        "com.example.uilayer.fileProvider",
                        audioFile);
                recordAudioIntent.putExtra(MediaStore.EXTRA_OUTPUT, audioUri);
                Log.d("Audio", "started: " + audioUri.toString());

                startRecordActivity();
            }
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // Ensure that there's a camera activity to handle the intent
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                videoFile = createTempFile(TYPE_VIDEO);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (isAvailable(getApplicationContext(), takeVideoIntent) && takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        "com.example.uilayer.fileProvider",
                        videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                Log.d("image", "started: " + videoURI.toString());
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                Log.d("Video", "started: ");
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createTempFile(TYPE_IMAGE);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.uilayer.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d("image", "started: " + photoURI.toString());

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createTempFile(String type) throws IOException {
        String prefix = "FILE";
        String suffix = ".jpg";
        switch (type) {
            case TYPE_IMAGE:
                prefix = "IMG_";
                suffix = ".jpg";
                break;
            case TYPE_VIDEO:
                prefix = "VID_";
                suffix = ".mp4";
                break;
            case TYPE_AUDIO:
                prefix = "AUD_";
                suffix = ".amr";
                break;

        }
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = prefix + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // File storageDir = Environment.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File tempFile = File.createTempFile(
                imageFileName,  /* prefix */
                suffix,         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentFilePath = tempFile.getAbsolutePath();
        Log.d("local path", "path: " + mCurrentFilePath);
        return tempFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == AUDIO_RECORD_CODE && resultCode == RESULT_OK) {
            upload(TYPE_AUDIO);
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            upload(TYPE_VIDEO);
            Log.d("Video", "onActivityResult: " + videoUri.toString());
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Uri imageUri = intent.getData();
            //Log.d("image", "onActivityResult: " + imageUri.toString());
            upload(TYPE_IMAGE);
            /*try {
                // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Log.d("Video", "onActivityResult: " + mCurrentFilePath);
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentFilePath, options);
            } catch (Exception ex) {
                ex.printStackTrace();
            }*/
        } else {
            super.onActivityResult(requestCode,
                    resultCode, intent);
        }


    }

    void upload(final String attachmentType) {
        Log.d("upload", "in");
        uploadFilePath = mCurrentFilePath;
        switch (attachmentType) {
            case TYPE_IMAGE:
                uploadFileMime = "image/jpeg";
                uploadFileExtension = ".jpg";
                break;
            case TYPE_VIDEO:
                uploadFileMime = "video/mp4";
                uploadFileExtension = ".mp4";
                break;
            case TYPE_AUDIO:
                uploadFileMime = "audio/amr";
                uploadFileExtension = ".amr";
                break;

        }
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                CREATE_MSG_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        Log.d("response", resultResponse);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    // String result = new String(networkResponse.data);
                    try {
                        // JSONObject response = new JSONObject(result);
                        //String status = response.getString("status");
                        String status = ("status");
                        // String message = response.getString("message");
                        String message = ("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }, new VolleyMultipartRequest.MultipartProgressListener() {
            @Override
            public void transferred(long transferred, int progress) {
                Log.d("upload", "transferred: " + transferred + "   " + "progress: " + progress);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();
                params.put(KEY_CONTENT, "THis is a content");
                params.put(KEY_TYPE, attachmentType);
                params.put(KEY_TICKET_ID, ticketId);
                params.put(KEY_SCHOOL_ID, String.valueOf(com.example.domainlayer.temp.DataHolder.
                        getInstance(MessageActivity.this).getUser().getSchoolId()));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new ArrayMap<>();
                try {
                    File sourceFile = new File(uploadFilePath);
                    params.put("file", new DataPart("file_avatar" + uploadFileExtension,
                            fullyReadFileToBytes(sourceFile),
                            uploadFileMime, sourceFile.length()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(multipartRequest);

    }


    @SuppressWarnings({"TryFinallyCanBeTryWithResources", "NewApi"})
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        Exception exception = null;
        FileInputStream fis = new FileInputStream(f);
        try {
            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        } finally {

            if (exception != null) {
                try {
                    fis.close();
                } catch (Throwable t) {
                    exception.addSuppressed(t);
                }
            } else {

                fis.close();
            }
        }

        return bytes;
    }

    void startRecordActivity() {
        startActivityForResult(new Intent(this, RecordAudioActivity.class).putExtra("path", mCurrentFilePath),
                AUDIO_RECORD_CODE);
    }

    public class AttachmentMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        AttachmentMenuClickListener(int position) {
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
                    startActivity(new Intent(MessageActivity.this, AddDocumentActivity.class));
                    return true;
            }
            return false;
        }
    }
}
