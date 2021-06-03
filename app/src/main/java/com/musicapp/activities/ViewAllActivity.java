package com.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.musicapp.R;
import com.musicapp.adapter.ViewAllAdapter;
import com.musicapp.models.DataPojo;
import com.musicapp.models.ExplorePojo;
import com.musicapp.models.ViewAllPojo;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllActivity extends PlayerBaseActivity implements View.OnClickListener {
    Context context = this;
    private ImageView back;
    private RecyclerView viewall_recycler;
    private TextView title;
    ViewAllAdapter viewAllAdapter;
    ArrayList<songsPojo> most_popular_list = new ArrayList<>();
    ArrayList<songsPojo> meditation_list = new ArrayList<>();
    int album_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        findId();
        if (getIntent() != null) {
            if (getIntent().hasExtra("most_popular")) {
                getMostPopular();
            } else if (getIntent().hasExtra("meditation")) {
                album_id = getIntent().getIntExtra("meditation_id", 0);
                getAllcategories();
            } else if (getIntent().hasExtra("music")) {
                album_id = getIntent().getIntExtra("id", 0);
                getAllcategories();

            }
        }
    }


    private void getAllcategories() {

        try {
            Utility utility = Utility.getInstance(this);

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Call<RestResponse<DataPojo>> callApi = apiInterface.getSongList(album_id);
            callApi.enqueue(new Callback<RestResponse<DataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<DataPojo>> call, Response<RestResponse<DataPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        DataPojo dataPojo = response.body().data();

                        if (dataPojo.getCategoryPojoArrayList() != null && dataPojo.getCategoryPojoArrayList().size() > 0) {

                            meditation_list.addAll(dataPojo.getCategoryPojoArrayList().get(0).getSongs());

                            Log.e("MEDITAION_LIST", String.valueOf(meditation_list.size()));

                            setAdapter(meditation_list);
                        } else {

                        }

                    } else {
                        Toast.makeText(ViewAllActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<DataPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getMostPopular() {

        try {
            Utility utility = Utility.getInstance(context);

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
            Call<RestResponse<ViewAllPojo>> callApi = apiInterface.mostPopular();
            callApi.enqueue(new Callback<RestResponse<ViewAllPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<ViewAllPojo>> call, Response<RestResponse<ViewAllPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_code", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        // most_popular_list.clear();
                        most_popular_list.addAll(response.body().getData().getSongs());

                        Log.e("sizeeeeeeeeeeeeee", String.valueOf(most_popular_list.size()));

                        if (most_popular_list.size() > 0) {
                            setAdapter(most_popular_list);

                        } else {
                        }


                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<ViewAllPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setAdapter(ArrayList<songsPojo> most_popular_list) {
        viewAllAdapter = new ViewAllAdapter(context, most_popular_list);
        viewall_recycler.setAdapter(viewAllAdapter);
    }

    private void findId() {
        viewall_recycler = findViewById(R.id.viewall_recycler);
        back = findViewById(R.id.back);
        viewall_recycler.setLayoutManager(new GridLayoutManager(context, 3));
        viewall_recycler.setHasFixedSize(true);
        title = findViewById(R.id.title);
        title.setText("All Songs");
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                finish();
                break;
        }
    }
}