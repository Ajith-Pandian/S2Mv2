package com.wowconnect.ui.profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.domain.network.VolleyMultipartRequest;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.DbUser;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.landing.LandingActivity;
import com.wowconnect.ui.manage.ManageTeachersActivity;
import com.wowconnect.ui.manage.SelectSchoolActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.EasyImageConfig;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ProfileUpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_image)
    ImageView profileImage;
    @BindView(R.id.edit_text_update_first_name)
    EditText textFirstName;
    @BindView(R.id.edit_text_update_last_name)
    EditText textLastName;
    @BindView(R.id.edit_text_update_email)
    EditText textEmail;
    @BindView(R.id.edit_text_update_phone)
    EditText textPhone;
    @BindView(R.id.edit_text_dob)
    EditText textDob;
    @BindView(R.id.edit_text_anniversary)
    EditText textAnniversary;
    @BindView(R.id.radio_male)
    AppCompatRadioButton maleButton;
    @BindView(R.id.radio_female)
    AppCompatRadioButton femaleButton;
    @BindView(R.id.fab_camera)
    FloatingActionButton cameraButton;
    @BindView(R.id.til_first_name)
    TextInputLayout tilFirstName;
    DbUser user;
    boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        ButterKnife.bind(this);
        toolbar.setTitle("Update Profile");
        setSupportActionBar(toolbar);
        loadUserData();
        if (getIntent() != null)
            isFirstTime = getIntent().getBooleanExtra(Constants.KEY_IS_FIRST_LOGIN, false);
    }

    void loadUserData() {
        user = new DataBaseUtil(this).getUser(SharedPreferenceHelper.getUserId());
        textFirstName.setText(user.getFirstName());
        textLastName.setText(user.getLastName());
        textEmail.setText(user.getEmail());
        textPhone.setText(user.getPhoneNum());
        textDob.setText(user.getDob());
        textAnniversary.setText(user.getAnniversary());

        if (user.getGender() != null) {
            if (user.getGender().equals("Male")) {
                maleButton.setChecked(true);
                gender = "Male";
            } else
                femaleButton.setChecked(true);
            gender = "Female";
        }
        if (user.getAvatar() != null && !user.getAvatar().equals("")) {
            Picasso.with(this).load(user.getAvatar()).into(profileImage);
        }
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dispatchTakePictureIntent();
                getNewImage();
                Log.d("Token", "Old token  " + FirebaseInstanceId.getInstance().getToken());


                try {
                    FirebaseInstanceId.getInstance().deleteToken("s2mv2-76810","");
                    //FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FirebaseInstanceId.getInstance().getId();
                // Now manually call onTokenRefresh()
                Log.d("Token", "New token  " + FirebaseInstanceId.getInstance().getToken());

            }
        });

        textDob.setKeyListener(null);
        textDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(isDob = true);
            }
        });
        textAnniversary.setKeyListener(null);
        textAnniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(isDob = false);
            }
        });

    }

    String gender;
    boolean isDob;

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((AppCompatRadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender = "Male";
                break;
            case R.id.radio_female:
                if (checked)
                    gender = "Female";
                break;
        }
    }

    //TODO:Simple date format
    void showDatePicker(boolean isDob) {
        Bundle bundle = new Bundle();
        int selectedYear = 0, selectedMonth = 0, selectedDay = 0;
        String dateString;
        if (isDob) {
            dateString = getTextFromEditText(textDob);
        } else {
            dateString = getTextFromEditText(textAnniversary);
        }

        DateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            date = (Date) formatter.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (!dateString.isEmpty()) {
                selectedYear = calendar.get(Calendar.YEAR);
                selectedMonth = calendar.get(Calendar.MONTH);
                selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
            }
            if (selectedYear != 0) {
                bundle.putInt("selected_year", selectedYear);
                bundle.putInt("selected_month", selectedMonth);
                bundle.putInt("selected_day", selectedDay);
            }
        }
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String selectedDate = year + "-" + month + "-" + dayOfMonth;
        if (isDob) {
            textDob.setText(selectedDate);
        } else
            textAnniversary.setText(selectedDate);

    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public void onStart() {
            super.onStart();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                getDialog().getWindow().setAttributes(params);
            }
        }

        final int DEFAULT_YEAR = 2000, DEFAULT_MONTH = 5, DEFAULT_DAY = 15;
        int selectedYear, selectedMonth, selectedDay;

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                selectedYear = bundle.getInt("selected_year", DEFAULT_YEAR);
                selectedMonth = bundle.getInt("selected_month", DEFAULT_MONTH);
                selectedDay = bundle.getInt("selected_day", DEFAULT_DAY);
            }
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), day, month, year);
            datePickerDialog.updateDate(selectedYear, selectedMonth, selectedDay);
            return datePickerDialog;

        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createTempFile(Constants.TYPE_IMAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

    static final int REQUEST_TAKE_PHOTO = 1;

    private File createTempFile(String type) throws IOException {
        String prefix = "FILE";
        String suffix = ".jpg";
        switch (type) {
            case Constants.TYPE_IMAGE:
                prefix = "IMG_";
                suffix = ".jpg";
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

    String mCurrentFilePath;


    void getNewImage() {


        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }
        EasyImage.configuration(this)
                .setImagesFolderName("EasyImageExp").saveInAppExternalFilesDir();
        EasyImage.openChooserWithGallery(ProfileUpdateActivity.this, "Take image", EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY);

        //  EasyImage.openChooserWithGallery(ProfileUpdateActivity.this, "Take image", EasyImageConfig.REQ_SOURCE_CHOOSER);

    }

    void updateUserDetails() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
                mCurrentFilePath = file.getAbsolutePath();
                Log.d("Image", "onImagePicked: " + mCurrentFilePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                profileImage.setImageBitmap(myBitmap);
              /*  Bitmap myBitmap;
                try {
                    myBitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                    profileImage.setImageBitmap(myBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/
            }
        });
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Uri imageUri = intent.getData();
            //Log.d("image", "onActivityResult: " + imageUri.toString());
            //showNewImage();
        } else {
            super.onActivityResult(requestCode,
                    resultCode, data);
        }
    }

    void showNewImage() {
        if (mCurrentFilePath != null && !mCurrentFilePath.isEmpty()) {
            File imgFile = new File(mCurrentFilePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                profileImage.setImageBitmap(myBitmap);
            }
        } else {
            Log.d("showNewImage", "No Image Picked");
        }
    }

    private void uploadDetails() {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                Constants.UPDATE_PROFILE_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("onResponse", resultResponse);
                Utils.getInstance().showToast("Updated Successfully");
                if (isFirstTime) {
                    doFirstTimeSetup();
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onResponse", error.toString());
                error.printStackTrace();
            }
        }, new VolleyMultipartRequest.MultipartProgressListener() {
            @Override
            public void transferred(long transferred, int progress) {
                Log.d("uploadDetails", "transferred: " + transferred + "   " + "progress: " + progress);
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();
                params.put(Constants.KEY_FIRST_NAME, getTextFromEditText(textFirstName));
                if (!getTextFromEditText(textLastName).isEmpty())
                    params.put(Constants.KEY_LAST_NAME, getTextFromEditText(textLastName));
                if (!getTextFromEditText(textEmail).isEmpty())
                    params.put(Constants.KEY_EMAIL, getTextFromEditText(textEmail));
                if (!getTextFromEditText(textDob).isEmpty())
                    params.put(Constants.KEY_DOB, getTextFromEditText(textDob));
                if (!getTextFromEditText(textAnniversary).isEmpty())
                    params.put(Constants.KEY_ANNIVERSARY, getTextFromEditText(textAnniversary));
                if (gender != null && !gender.isEmpty())
                    params.put(Constants.KEY_GENDER, gender);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new ArrayMap<>();
                headers.put(Constants.KEY_ACCEPT, Constants.HEADER_ACCEPT);
                headers.put(Constants.KEY_AUTHORIZATION, SharedPreferenceHelper.getAccessToken());
                return headers;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new ArrayMap<>();
                if (mCurrentFilePath != null && !mCurrentFilePath.isEmpty()) {
                    try {
                        File sourceFile = new File(mCurrentFilePath);
                        params.put("profile_picture", new DataPart("avatar.jpg",
                                fullyReadFileToBytes(sourceFile),
                                "image/jpeg", sourceFile.length()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(multipartRequest);
    }


    String getTextFromEditText(EditText editText) {
        return editText.getText().toString();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user.getId() == SharedPreferenceHelper.getUserId()) {
            getMenuInflater().inflate(R.menu.menu_profile_update, menu);
            return true;
        } else
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save_profile:
                validateInputsAndUpdate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void validateInputsAndUpdate() {
        if (!getTextFromEditText(textFirstName).isEmpty())
            uploadDetails();
        else Utils.getInstance().showToast("FirstName cannot be empty");


    }

    void doFirstTimeSetup() {
        if (user.getType().equals(Constants.USER_TYPE_S2M_ADMIN))
            launchSelectSchool();
        else if (user.getType().equals(Constants.USER_TYPE_SCHOOL)) {
            ArrayList<String> roles =  new NewDataParser().getUserRoles(this, SharedPreferenceHelper.getUserId());
            if (roles.contains(Constants.ROLE_SCL_ADMIN) ||
                    roles.contains(Constants.ROLE_COORDINATOR))
                launchManageTeachersAndSections();
            else if (roles.contains(Constants.ROLE_TEACHER))
                launchLanding();
        } else
            launchLanding();

    }

    void launchSelectSchool() {
        startActivity(new Intent(this, SelectSchoolActivity.class).putExtra("isFirstTime", true));
    }

    void launchManageTeachersAndSections() {
        startActivity(new Intent(this, ManageTeachersActivity.class)
                .putExtra("isFirstTime", true)
                .putExtra("isTeachers", true));
    }

    void launchLanding() {
        startActivity(new Intent(this, LandingActivity.class));
    }
}
