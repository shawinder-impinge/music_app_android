package com.impinge.soul.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.impinge.soul.R;
import com.impinge.soul.models.ChangePassModel;
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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_old_pass, et_new_password, et_cp;
    private TextView change_password, title;
    Context context = this;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        findId();
    }

    private void findId() {
        change_password = findViewById(R.id.change_password);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        title.setText("Change Password");
        et_old_pass = findViewById(R.id.et_old_pass);
        et_new_password = findViewById(R.id.et_new_password);
        et_cp = findViewById(R.id.et_cp);
        change_password.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_password:

                if (isValidate()) {

                    if (Constants.isNetworkAvailable(context)) {
                        callChangePass();
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


    private void callChangePass() {
        {
            Utility utility = Utility.getInstance(this);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("old_password", et_old_pass.getText().toString());
            hashMap.put("new_password", et_new_password.getText().toString());

            Log.e("CHANGEPASS_DATA", String.valueOf(hashMap));
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(ChangePasswordActivity.this).create(APIInterface.class);


            Call<RestResponse<ChangePassModel>> callApi = apiInterface.changePassword(hashMap);
            callApi.enqueue(new Callback<RestResponse<ChangePassModel>>() {
                @Override
                public void onResponse(Call<RestResponse<ChangePassModel>> call, Response<RestResponse<ChangePassModel>> response) {


                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        utility.hideLoading();
                        Toast.makeText(ChangePasswordActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        et_new_password.setText("");
                        et_old_pass.setText("");
                        et_cp.setText("");

                        Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        utility.hideLoading();
                    }
                }

                @Override
                public void onFailure(Call<RestResponse<ChangePassModel>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        }


    }

    private boolean isValidate() {


        if (et_old_pass.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter old password");

            return false;

        } else if (et_new_password.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter new password");


            return false;

        } else if (et_cp.getText().toString().trim().isEmpty()) {


            Alert.showWarningAlert(context, "Please enter confirm new password");


            return false;

        } else if (!(et_new_password.getText().toString().trim().equals(et_cp.getText().toString().trim()))) {

            Alert.showWarningAlert(context, "Password do not match");

            return false;

        }

        return true;
    }
}