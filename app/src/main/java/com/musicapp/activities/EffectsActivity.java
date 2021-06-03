package com.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.musicapp.R;
import com.musicapp.adapter.AnimalEffectAdapter;
import com.musicapp.adapter.EffectSubCategoryAdapter;
import com.musicapp.adapter.WaterEffectAdapter;
import com.musicapp.adapter.WeatherCategoryAdapter;
import com.musicapp.adapter.WeatherEffectAdapter;
import com.musicapp.adapter.WhiteNoiseEffectAdapter;
import com.musicapp.fragments.EqualizerFragment;
import com.musicapp.models.CategoryPojoModel;
import com.musicapp.models.DataPojo;
import com.musicapp.models.EffectsAlbumPojo;
import com.musicapp.models.EffectsPojo;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.PlayerUtil;
import com.musicapp.util.Utility;
import com.musicapp.util.sweetdialog.Alert;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EffectsActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    private TextView title;
    private ImageView back;
    private RecyclerView water_recylerview, weather_recylerview, white_noise_recylerview, animal_recylerview;
    WaterEffectAdapter waterEffectAdapter;
    WeatherCategoryAdapter weatherEffectAdapter;
    WhiteNoiseEffectAdapter whiteNoiseEffectAdapter;
    AnimalEffectAdapter animalEffectAdapter;
    private int count_request_popup = 0;
    boolean isDownloadComplete;
    Uri nature_uri;


    ArrayList<EffectsAlbumPojo> effectsPojoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);
        findId();
        setAdapter();
        getEffectCategories();
    }


    private void setAdapter() {


        if (weatherEffectAdapter == null) {
            weatherEffectAdapter = new WeatherCategoryAdapter(context, effectsPojoArrayList);
            weather_recylerview.setAdapter(weatherEffectAdapter);
        } else {
            weatherEffectAdapter.notifyDataSetChanged();
        }
        weatherEffectAdapter.onEffectClick(new WeatherCategoryAdapter.EffectClick() {
            @Override
            public void click(String id, String pos, String url, String desc, String title) {
//                song_audio_url=url;

                if (checkPermissionn()) {
                    if (isDownloadComplete) {

                        Toast.makeText(EffectsActivity.this, getResources().getString(R.string.download_inProgress), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isDownloadComplete = true;

                    downloadFile(url, desc, title);


                }

            }
        });

    }

    private void findId() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        title.setText("Nature Effect");
        weather_recylerview = findViewById(R.id.weather_recylerview);


        weather_recylerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        weather_recylerview.setHasFixedSize(true);

        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getEffectCategories() {
        try {
            Utility utility = Utility.getInstance(this);
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Call<RestResponse<DataPojo>> callApi = apiInterface.getEffectCategories();
            callApi.enqueue(new Callback<RestResponse<DataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<DataPojo>> call, Response<RestResponse<DataPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        effectsPojoArrayList.addAll(response.body().getData().getEffect_data().get(0).getEffect_album());

                        setAdapter();

                    } else {

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


    private boolean checkPermissionn() {
        if (Build.VERSION.SDK_INT >= 23) {

            int hasReadPermission = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasWritePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            ArrayList<String> permissionList = new ArrayList<String>();

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 2);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        Alert.showLog("denied", permission);

                    } else {
                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                            Alert.showLog("allowed", permission);
                        } else {
                            if (count_request_popup == 0) {
                                count_request_popup = count_request_popup + 1;
                                Alert.setOnOKClickListner(new Alert.OnOKClickListner() {
                                    @Override
                                    public void onOK(boolean yes) {
                                        count_request_popup = 0;
                                    }
                                });
                                Alert.showWarningAlertWithResponse(context, getString(R.string.never_ask_again));
                            }
                        }
                    }
                }
                break;
        }
    }


    private void downloadFile(String song_audio_url, String desc, String title) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(song_audio_url));
        Log.e("URLLLLLLLLLLLLL", song_audio_url);
        request.setDescription(desc);
        request.setTitle(title);
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".mp3");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(this.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            isDownloadComplete = false;

            String path = Environment.DIRECTORY_DOWNLOADS + "/" + title + ".mp3";

            nature_uri = Uri.fromFile(new File(path));

        }
    };


}