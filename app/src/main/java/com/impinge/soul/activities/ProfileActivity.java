package com.impinge.soul.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.impinge.soul.BuildConfig;
import com.impinge.soul.R;
import com.impinge.soul.models.UserDataPojo;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.Controller;
import com.impinge.soul.util.PreferenceData;
import com.impinge.soul.util.SharedPreference;
import com.impinge.soul.util.Utility;
import com.impinge.soul.util.sweetdialog.Alert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    CircleImageView profile_pic;
    private ImageView edit, edit_icon, back;
    private TextView submit_profile, title;
    private EditText et_username, et_email, et_phone, et_work, et_gender;
    private RelativeLayout gender_lay;
    private RadioGroup gender_edit_lay;
    private RadioButton rb_male, rb_female;
    private TextView camera, gallery, cancel;
    Animation up, down;
    ViewGroup hiddenPanel;
    private boolean isPanelShown = false;
    RelativeLayout relativeLayout;
    View root;
    private File camProfilePic;
    private int count_request_popup = 0;
    private String gender = "", userId = "";
    MultipartBody.Part body;
    RequestBody requestFile;
    File mSaveBit;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findId();
        userId = SharedPreference.fetchPrefenceData(ProfileActivity.this, PreferenceData.USER_ID);

        View someView = findViewById(R.id.edit_profile);
        if (someView != null) {
            root = someView.getRootView();
        }
        setEnable(false);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            checkPermissionn();
            takePermissions(view);
        }else {
            checkPermissionn();
        }



        viewProfile();
    }

    private void viewProfile() {
        try {
            Utility utility = Utility.getInstance(context);
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
            Call<RestResponse<UserDataPojo>> callApi = apiInterface.viewprofile(SharedPreference.fetchPrefenceData(ProfileActivity.this, PreferenceData.USER_ID));
            callApi.enqueue(new Callback<RestResponse<UserDataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<UserDataPojo>> call, Response<RestResponse<UserDataPojo>> response) {
                    utility.hideLoading();
                    Log.e("response_msg", String.valueOf(response));
                    if (response.code() == 200) {
                        String username = response.body().getData().getUser().getUsername();
                        String email = response.body().getData().getUser().getEmail();
                        String phone = response.body().getData().getUser().getPhone();
                        String work = response.body().getData().getUser().getWork();
                        String gender = response.body().getData().getUser().getGender();
                        String image = response.body().getData().getUser().getImage();
                        et_username.setText(username);
                        et_email.setText(email);
                        et_phone.setText(phone);
                        et_work.setText(work);
                        et_gender.setText(gender);
                        if (image != null && !image.isEmpty()) {


                            Glide.with(context).load(image).into(profile_pic);

                        } else {

                            Glide.with(context).load(R.drawable.user_placeholder).into(profile_pic);

                        }

                        //Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RestResponse<UserDataPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean checkPermissionn() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);


            ArrayList<String> permissionList = new ArrayList<String>();


            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 2);
                return false;
            }
        }
        return true;
    }

    private void findId() {
        camera = findViewById(R.id.camera);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        profile_pic = findViewById(R.id.profile_pic);
        gallery = findViewById(R.id.gallery);
        cancel = findViewById(R.id.cancel);
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        edit_icon = findViewById(R.id.edit_icon);
        title = findViewById(R.id.title);
        title.setText("Profile");
        gender_edit_lay = findViewById(R.id.gender_edit_lay);
        gender_lay = findViewById(R.id.gender_lay);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_work = findViewById(R.id.et_work);
        et_gender = findViewById(R.id.et_gender);
        submit_profile = findViewById(R.id.submit_profile);
        edit.setVisibility(View.VISIBLE);
        submit_profile.setOnClickListener(this);
        edit.setOnClickListener(this);
        back.setOnClickListener(this);
        edit_icon.setOnClickListener(this);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        cancel.setOnClickListener(this);

        up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        relativeLayout = (RelativeLayout) findViewById(R.id.layer_dummy);
        hiddenPanel = (LinearLayout) findViewById(R.id.show_layout);
        gender_edit_lay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        gender = "male";
                        // do operations specific to this selection
                        break;
                    case R.id.rb_female:
                        gender = "female";
                        // do operations specific to this selection
                        break;

                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edit:

                submit_profile.setVisibility(View.VISIBLE);
                gender_edit_lay.setVisibility(View.VISIBLE);
                gender_lay.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                edit_icon.setVisibility(View.VISIBLE);

                if (et_gender.getText().toString().equalsIgnoreCase("male")) {

                    rb_male.setChecked(true);
                    rb_female.setChecked(false);
                } else if (et_gender.getText().toString().equalsIgnoreCase("female")) {


                    rb_female.setChecked(true);
                    rb_male.setChecked(false);
                }
                setEnable(true);


                break;

            case R.id.submit_profile:

                if (Constants.isNetworkAvailable(context)) {

                    callEditProfile();

                } else {
                    Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                }
                break;


            case R.id.back:
                finish();

                break;


            case R.id.edit_icon:
                slideUpDown();

                break;

            case R.id.camera:
                cameraIntent();
                closePanel();

                break;

            case R.id.gallery:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, 505);
                closePanel();

                break;

            case R.id.cancel:
                closePanel();

                break;
        }

    }

    private void callEditProfile() {

        Log.e("callEditProfile", "callEditProfile");
        try {
            Utility utility = Utility.getInstance(context);

            RequestBody username = RequestBody.create(MediaType.parse("text/plain"), et_username.getText().toString());
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), et_email.getText().toString());
            RequestBody genderr = RequestBody.create(MediaType.parse("text/plain"), gender);
            RequestBody firstname = RequestBody.create(MediaType.parse("text/plain"), "first");
            RequestBody lastname = RequestBody.create(MediaType.parse("text/plain"), "last");
            RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), et_phone.getText().toString());
            RequestBody work = RequestBody.create(MediaType.parse("text/plain"), et_work.getText().toString());


            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
            Call<RestResponse<UserDataPojo>> callApi = apiInterface.editProfile2(username, email, genderr,
                    firstname, lastname, phone, work, body);
            callApi.enqueue(new Callback<RestResponse<UserDataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<UserDataPojo>> call, Response<RestResponse<UserDataPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        edit.setVisibility(View.VISIBLE);
                        gender_edit_lay.setVisibility(View.GONE);
                        gender_lay.setVisibility(View.VISIBLE);
                        submit_profile.setVisibility(View.GONE);
                        setEnable(false);
                        edit_icon.setVisibility(View.GONE);

                        String username = response.body().getData().getUser().getUsername();
                        String email = response.body().getData().getUser().getEmail();
                        String phone = response.body().getData().getUser().getPhone();
                        String work = response.body().getData().getUser().getWork();
                        String gender = response.body().getData().getUser().getGender();
                        String image = response.body().getData().getUser().getImage();
                        et_username.setText(username);
                        et_email.setText(email);
                        et_phone.setText(phone);
                        et_work.setText(work);
                        et_gender.setText(gender);
                        SharedPreference.savePreferenceData(ProfileActivity.this, PreferenceData.USERNAME, username);


                        if (image != null && !image.isEmpty()) {
                            Glide.with(context).load(image).into(profile_pic);
                        } else {
                            Glide.with(context).load(R.drawable.user_placeholder).into(profile_pic);

                        }

                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        //Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RestResponse<UserDataPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                camProfilePic = new File(Controller.getImagePath(), timeStamp + "post.png");
                Uri photoURI = FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", camProfilePic);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 502);
            } catch (Exception e) {
                e.printStackTrace();
                Alert.showLog("Exception", "openCamera Exception-- " + e.toString() + "  " + e.getMessage());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 502:
                if (resultCode == RESULT_OK & camProfilePic != null) {
                    String path = camProfilePic.getPath();
                    Log.e("CAMERA_PATH", path);
                    setImageafterCrop(path);
//                    Intent intent = new Intent(this, Croping.class);
//                    intent.putExtra("PROFILE", path);
//                    intent.putExtra("CAMERA", "CAMERA");
//                    startActivityForResult(intent, 303);
                }
                break;

            case 505:
                if (resultCode == RESULT_OK & data != null) {
                    Uri uri1 = data.getData();
                    String path1 = getRealPathFromURI(uri1);
                    Log.e("GALLERY_PATH", path1);
                    setImageafterCrop(path1);
//                    Intent intent = new Intent(this, Croping.class);
//                    intent.putExtra("PROFILE", path1);
//                    startActivityForResult(intent, 303);
                }
                break;

            case 303:
                if (resultCode == RESULT_OK & data != null) {
                    setImageafterCrop(data.getExtras().get("PATH").toString());
                }
                break;

            case 100:

                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            Toast.makeText(context, "permission granted in 11", Toast.LENGTH_SHORT).show();
                        } else {
                            takePermission();
                        }
                    }
                }

                break;
        }
    }

    private void setImageafterCrop(String s) {
        mSaveBit = new File(s);

        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mSaveBit);
        body = MultipartBody.Part.createFormData("image", mSaveBit.getName(), requestFile);


        String filePath = mSaveBit.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        profile_pic.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 360, 360, false));

    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0) {

            if (requestCode == 101) {
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (readExternalStorage) {
                    Toast.makeText(context, "Read permission is granted in android 10 or below", Toast.LENGTH_SHORT).show();
                } else {
                    takePermission();
                }
            }

        }


        switch (requestCode) {
            case 2:
                for (String permission : permissions) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//                        Alert.showLog("denied", permission);
//                    } else {
//                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
//                            Alert.showLog("allowed", permission);
//                        } else {
//                            if (count_request_popup == 0) {
//                                count_request_popup = count_request_popup + 1;
//                                Alert.setOnOKClickListner(new Alert.OnOKClickListner() {
//                                    @Override
//                                    public void onOK(boolean yes) {
//                                        count_request_popup = 0;
//                                    }
//                                });
//                                Alert.showWarningAlertWithResponse(context, getString(R.string.never_ask_again));
//                            }
//                        }
//                    }
                }
                break;
        }
    }


    private void setEnable(boolean bool) {
        et_username.setEnabled(bool);
        et_email.setEnabled(bool);
        et_phone.setEnabled(bool);
        et_work.setEnabled(bool);
        et_gender.setEnabled(bool);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        hiddenPanel.getGlobalVisibleRect(viewRect);
        if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            if (isPanelShown) {
                closePanel();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);

    }

    private void closePanel() {
        if (isPanelShown) {
            relativeLayout.setVisibility(View.GONE);

            hiddenPanel.startAnimation(down);
            hiddenPanel.setVisibility(View.INVISIBLE);
            root.setBackgroundColor(getResources().getColor(R.color.white));
            isPanelShown = false;
        }
    }

    private void slideUpDown() {
        if (!isPanelShown) {
            relativeLayout.setVisibility(View.VISIBLE);
            hiddenPanel.startAnimation(up);
            hiddenPanel.setVisibility(View.VISIBLE);
            root.setBackgroundColor(getResources().getColor(R.color.line));
            isPanelShown = true;
        } else {
            closePanel();
        }
    }


    public void takePermission() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 100);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 100);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 101);
        }
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readInternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return readInternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void takePermissions(View view) {

        if (isPermissionGranted()) {
            Toast.makeText(context, "permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            takePermission();
        }
    }

}