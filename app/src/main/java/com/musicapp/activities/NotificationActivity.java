package com.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicapp.R;
import com.musicapp.adapter.NotificationAdapter;
import com.musicapp.models.NotificationData;
import com.musicapp.models.NotificationPojo;
import com.musicapp.models.ViewAllPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    Context context = this;
    private TextView title;
    RecyclerView notification_recycler;
    NotificationAdapter notificationAdapter;
    ImageView back;
    ArrayList<NotificationData> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findId();
        getNotifications();
    }


    private void getNotifications() {

        try {
            Utility utility = Utility.getInstance(context);

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
            Call<RestResponse<NotificationPojo>> callApi = apiInterface.getNotifications();
            callApi.enqueue(new Callback<RestResponse<NotificationPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<NotificationPojo>> call, Response<RestResponse<NotificationPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_code", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        notificationList.clear();
                        notificationList.addAll(response.body().getData().getNotifications());

                        if (notificationList.size() > 0) {
                            setAdapter(notificationList);

                        } else {
                        }


                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<NotificationPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void setAdapter(ArrayList<NotificationData> notificationList) {
        notificationAdapter = new NotificationAdapter(context, notificationList);
        notification_recycler.setAdapter(notificationAdapter);

    }

    private void findId() {
        notification_recycler = findViewById(R.id.notification_recycler);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        title.setText("Notifications");
        notification_recycler.setLayoutManager(new LinearLayoutManager(context));
        notification_recycler.setHasFixedSize(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}