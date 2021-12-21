package com.impinge.soul.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.impinge.soul.R;
import com.impinge.soul.models.User;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.PreferenceData;
import com.impinge.soul.util.SharedPreference;
import com.impinge.soul.util.Utility;
import com.impinge.soul.util.sweetdialog.Alert;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    private static ProgressDialog mProgressDialog;
    Context context = this;
    private TextView signup, already_have_account;
    private EditText et_username, et_email, et_password, et_conf_password;
    VideoView videoview;
    String video_link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fullWindow();
        findId();

        if (getIntent().hasExtra("link")) {

            video_link = getIntent().getStringExtra("link");


        }
    }


    private void findId() {
        signup = findViewById(R.id.signup);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_conf_password = findViewById(R.id.et_conf_password);
        already_have_account = findViewById(R.id.already_have_account);
        signup.setOnClickListener(this);
        already_have_account.setOnClickListener(this);
        already_have_account.setText(Html.fromHtml("<u>Login"));


        playVideoBackground();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:


                if (isValidate()) {

                    if (Constants.isNetworkAvailable(context)) {

                        callSignUpApi();


                    } else {
                        Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                    }
                }

                break;
            case R.id.already_have_account:
                redirectToLogin();
                break;
        }
    }

    private void callSignUpApi() {

        try {
            Utility utility = Utility.getInstance(this);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("username", et_username.getText().toString());
            hashMap.put("email", et_email.getText().toString());
            hashMap.put("password", et_password.getText().toString());
            Log.e("SIGNUP_DATA", String.valueOf(hashMap));
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(SignUpActivity.this).create(APIInterface.class);
            Call<RestResponse<User>> callApi = apiInterface.signup(hashMap);
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        User user = response.body().data();

                        SharedPreference.savePreferenceData(SignUpActivity.this, PreferenceData.USER_ID, response.body().getId());
                        SharedPreference.savePreferenceData(SignUpActivity.this, PreferenceData.TOKEN, user.token);
                        SharedPreference.savePreferenceData(SignUpActivity.this, PreferenceData.USERNAME, response.body().getUsername());

                       // Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                        replacScreen();
                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void replacScreen() {


        Intent i = new Intent(SignUpActivity.this, HearAboutActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
    }


    private boolean isValidate() {


        if (et_username.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter username");

            return false;

        } else if (et_email.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter email");


            return false;

        } else if (!et_email.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {


            Alert.showWarningAlert(context, "Please enter valid email");


            return false;

        } else if (et_password.getText().toString().trim().isEmpty()) {


            Alert.showWarningAlert(context, "Please enter password");


            return false;

        } else if (et_password.getText().toString().trim().length() < 6) {


            Alert.showWarningAlert(context, "Password length should not be less than 6 characters");

            return false;

        } else if (et_conf_password.getText().toString().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter confirm password");


            return false;

        } else if (!(et_password.getText().toString().trim().equals(et_conf_password.getText().toString().trim()))) {

            Alert.showWarningAlert(context, "Password do not match");


            return false;

        }

        return true;
    }

    private void redirectToLogin() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class).putExtra("link", video_link));
        overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideoBackground();

    }

    private void playVideoBackground() {
        videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.app_video);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoview.start();
            }
        });
    }


}