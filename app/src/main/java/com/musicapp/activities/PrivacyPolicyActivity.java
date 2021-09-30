package com.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.musicapp.R;
import com.musicapp.models.DataPojo;
import com.musicapp.models.PrivacyPolicyPojo;
import com.musicapp.models.User;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyPolicyActivity extends AppCompatActivity {
    Context context = this;
    private TextView title, privacy_policy;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        findId();
        callPrivacyPolicy();
    }

    private void callPrivacyPolicy() {
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

                        privacy_policy.setText(Html.fromHtml(privacy_text));

                       // Toast.makeText(PrivacyPolicyActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PrivacyPolicyActivity.this, response.message(), Toast.LENGTH_SHORT).show();

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
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        privacy_policy = findViewById(R.id.privacy_policy);
        title.setText("Privacy Policy");

        title.setTextColor(getResources().getColor(R.color.app_color));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}