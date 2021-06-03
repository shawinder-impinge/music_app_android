package com.musicapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musicapp.R;
import com.musicapp.adapter.SongListAdpter;
import com.musicapp.models.DataPojo;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteListActivity extends AppCompatActivity {
    private TextView title;
    Context context = this;
    RecyclerView recyclerview;
    SongListAdpter songListAdpter;
    private ImageView back;

    ArrayList<songsPojo> songsPojoArrayList = new ArrayList<>();
    LinearLayout mLvNoFavLayout;
    LinearLayout mLvFavListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiity_favourite);
        findId();
        setAdapter();
        fetchAlbumSongs();
    }

    private void setAdapter() {
        if (songListAdpter == null) {
            songListAdpter = new SongListAdpter(this, songsPojoArrayList);
            recyclerview.setAdapter(songListAdpter);
        } else {
            songListAdpter.notifyDataSetChanged();
        }

    }

    private void fetchAlbumSongs() {

        try {
            Utility utility = Utility.getInstance(this);

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Call<RestResponse<DataPojo>> callApi = apiInterface.getFavouriteSongList(SharedPreference.fetchPrefenceData(this, PreferenceData.USER_ID));
            callApi.enqueue(new Callback<RestResponse<DataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<DataPojo>> call, Response<RestResponse<DataPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        DataPojo dataPojo = response.body().data();

                        if (dataPojo.getSongs() != null && dataPojo.getSongs().size() > 0) {
                            mLvFavListLayout.setVisibility(View.VISIBLE);
                            mLvNoFavLayout.setVisibility(View.GONE);
                            songsPojoArrayList.clear();
                            songsPojoArrayList.addAll(dataPojo.getSongs());

                            setAdapter();

                        } else {
                            mLvFavListLayout.setVisibility(View.GONE);
                            mLvNoFavLayout.setVisibility(View.VISIBLE);
                        }


                        Toast.makeText(FavouriteListActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FavouriteListActivity.this, response.message(), Toast.LENGTH_SHORT).show();

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

    private void findId() {

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }*/

        mLvFavListLayout = findViewById(R.id.fav_list_layout);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        title.setText("Favourites");
        mLvNoFavLayout = findViewById(R.id.no_fav_id);
        recyclerview = findViewById(R.id.recyclerView);
        mLvFavListLayout.setVisibility(View.GONE);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setHasFixedSize(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}