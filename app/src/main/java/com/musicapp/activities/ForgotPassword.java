package com.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.musicapp.R;
import com.musicapp.models.User;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Constants;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;
import com.musicapp.util.sweetdialog.Alert;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    private TextView send, title;
    private EditText et_email;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findId();
    }

    private void findId() {
        send = findViewById(R.id.send);
        back = findViewById(R.id.back);
        et_email = findViewById(R.id.et_email);
        title = findViewById(R.id.title);
        send.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:

                if (isValidate()) {

                    if (Constants.isNetworkAvailable(context)) {
                        callForgotPassApi();
                    } else {
                        Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                    }
                }
                break;
            case R.id.back:
                onBackPressed();
                finish();
                break;
        }
    }

    private void callForgotPassApi() {

        Utility utility = Utility.getInstance(this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", et_email.getText().toString());

        Log.e("ForgotPassData", String.valueOf(hashMap));
        utility.showLoading(getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<RestResponse<User>> callApi = apiInterface.forgotpass(hashMap);
        callApi.enqueue(new Callback<RestResponse<User>>() {
            @Override
            public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                Log.e("response_msg", String.valueOf(response.code()));
                if (response.code() == 200) {
                    utility.hideLoading();
                    et_email.setText("");
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();

                } else {
                    utility.hideLoading();
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                Log.e("exception_msg", t.getMessage());
                utility.hideLoading();

            }
        });
    }

    private boolean isValidate() {

        if (et_email.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter email");

            return false;

        } else if (!et_email.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {


            Alert.showWarningAlert(context, "Please enter valid email");


            return false;

        }
        return true;
    }
}