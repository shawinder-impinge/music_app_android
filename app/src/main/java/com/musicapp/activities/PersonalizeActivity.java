package com.musicapp.activities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musicapp.R;
import com.musicapp.adapter.PersonalizedAdapter;
import com.musicapp.adapter.SongListAdpter;
import com.musicapp.models.PersonalizedModel;
import com.musicapp.models.User;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonalizeActivity extends BaseActivity implements View.OnClickListener {
    private TextView proceed;
    private ImageView cross;

    View selected_view;
    private TextView better_sleep, reduce_anxiety, develop_gratitude, increase_happiness, reduce_stress, improve_performance, build_selfesteem, programming;
    private RecyclerView recyclerview;
    private PersonalizedAdapter personalizedAdapter;

    private ArrayList<PersonalizedModel> personalizedModelArrayList = new ArrayList<PersonalizedModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize);
        fullWindow();
        findId();


        loadPersonalizedList();
        setAdapter();

    }

    private void loadPersonalizedList() {


        loadModel(1, getResources().getString(R.string.better_sleep));
        loadModel(2, getResources().getString(R.string.reduce_anxiety));
        loadModel(3, getResources().getString(R.string.develop_gratitude));
        loadModel(4, getResources().getString(R.string.increase_happiness));
        loadModel(5, getResources().getString(R.string.reduce_stress));
        loadModel(6, getResources().getString(R.string.improve_performance));
        loadModel(7, getResources().getString(R.string.build_self_esteem));
        loadModel(8, getResources().getString(R.string.subconsious_programming));
    }

    private void loadModel(int id, String value) {

        PersonalizedModel personalizedModel = new PersonalizedModel(id, value);
        personalizedModelArrayList.add(personalizedModel);

    }

    private void setAdapter() {

        if (personalizedAdapter == null) {
            personalizedAdapter = new PersonalizedAdapter(this, personalizedModelArrayList);
            recyclerview.setAdapter(personalizedAdapter);
        } else {
            personalizedAdapter.notifyDataSetChanged();
        }

    }


    private void findId() {
        recyclerview = findViewById(R.id.recyclerview);

        proceed = findViewById(R.id.proceed);
        cross = findViewById(R.id.cross);
        proceed.setOnClickListener(this);
        cross.setOnClickListener(this);
//        better_sleep = findViewById(R.id.better_sleep);
//        reduce_anxiety = findViewById(R.id.reduce_anxiety);
//        develop_gratitude = findViewById(R.id.develop_gratitude);
//        increase_happiness = findViewById(R.id.increase_happiness);
//        reduce_stress = findViewById(R.id.reduce_stress);
//        improve_performance = findViewById(R.id.improve_performance);
//        build_selfesteem = findViewById(R.id.build_selfesteem);
//        programming = findViewById(R.id.programming);
//
//        better_sleep.setOnClickListener(this);
//        reduce_anxiety.setOnClickListener(this);
//        develop_gratitude.setOnClickListener(this);
//        increase_happiness.setOnClickListener(this);
//        reduce_stress.setOnClickListener(this);
//        improve_performance.setOnClickListener(this);
//        build_selfesteem.setOnClickListener(this);
//        programming.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        selected_view = v;
        switch (v.getId()) {
            case R.id.proceed:

//
                callPersonalizedApi();
                break;
            case R.id.cross:
                finish();
                break;

        }
    }

    private void redirectToDashboard() {
        startActivity(new Intent(PersonalizeActivity.this, DashboardActivity.class));
        overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
    }

    private void callPersonalizedApi() {
        {
            Utility utility = Utility.getInstance(this);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user", SharedPreference.fetchPrefenceData(PersonalizeActivity.this, PreferenceData.USER_ID));
            hashMap.put("recommendation", personalizedAdapter.selectedList);

            Log.e("PERSONALIZED _DATA", String.valueOf(hashMap));
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(PersonalizeActivity.this).create(APIInterface.class);


            Call<RestResponse<User>> callApi = apiInterface.submitPersonalized(hashMap);
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        User user = response.body().data();

                        utility.hideLoading();
//                    SharedPreference.savePreferenceData(PersonalizeActivity.this, PreferenceData.USER_ID,response.body().getId());
//                    SharedPreference.savePreferenceData(PersonalizeActivity.this, PreferenceData.TOKEN,user.token);
                       // Toast.makeText(PersonalizeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        redirectToDashboard();
                    } else {
                        utility.hideLoading();
                    }
                }

                @Override
                public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        }


    }
}