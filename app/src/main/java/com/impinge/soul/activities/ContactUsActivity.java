package com.impinge.soul.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.impinge.soul.R;
import com.impinge.soul.models.PrivacyPolicyPojo;
import com.impinge.soul.models.User;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.Utility;
import com.impinge.soul.util.sweetdialog.Alert;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {
    Context context = this;
    private TextView title, contact_description, send;
    private EditText et_username, et_email, et_message;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        findId();
        callContactUs();

    }

    private void submitContactUs() {

        Utility utility = Utility.getInstance(this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("first_name", et_username.getText().toString());
        hashMap.put("email", et_email.getText().toString());
        hashMap.put("message", et_message.getText().toString());

        Log.e("params", String.valueOf(hashMap));
        utility.showLoading(getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<RestResponse<User>> callApi = apiInterface.forgotpass(hashMap);
        callApi.enqueue(new Callback<RestResponse<User>>() {
            @Override
            public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                Log.e("response_msg", String.valueOf(response.code()));
                if (response.code() == 200) {
                    utility.hideLoading();
                    et_username.setText("");
                    et_email.setText("");
                    et_message.setText("");
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

    private void callContactUs() {
        try {
            Utility utility = Utility.getInstance(this);

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Call<RestResponse<PrivacyPolicyPojo>> callApi = apiInterface.privacyPolicy("privacy_policy");
            callApi.enqueue(new Callback<RestResponse<PrivacyPolicyPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<PrivacyPolicyPojo>> call, Response<RestResponse<PrivacyPolicyPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        PrivacyPolicyPojo policyPojo = response.body().data();

                        String privacy_text = policyPojo.getPage().get(0).getDescription();

                        contact_description.setText(Html.fromHtml(privacy_text));

                       // Toast.makeText(ContactUsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ContactUsActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<PrivacyPolicyPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void findId() {
        et_message = findViewById(R.id.et_message);
        back = findViewById(R.id.back);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        title = findViewById(R.id.title);
        send = findViewById(R.id.send);
        contact_description = findViewById(R.id.contact_description);
        title.setText("GET IN TOUCH");
        title.setTextColor(getResources().getColor(R.color.app_color));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isValidate()) {

                    if (Constants.isNetworkAvailable(context)) {

                        submitContactUs();
                    } else {
                        Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                    }
                }
            }
        });

    }

    private boolean isValidate() {


        if (et_username.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter Name");

            return false;

        } else if (et_email.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter email");


            return false;

        } else if (!et_email.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {


            Alert.showWarningAlert(context, "Please enter valid email");


            return false;

        } else if (et_message.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter your query");

            return false;

        }

        return true;
    }


}