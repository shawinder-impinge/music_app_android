package com.impinge.soul.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.impinge.soul.R;
import com.impinge.soul.adapter.BinauralAdapter;
import com.impinge.soul.models.BinauralPojo;
import com.impinge.soul.models.songsPojo;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Utility;
import com.impinge.soul.util.sweetdialog.Alert;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BinaturalBeatActivity extends PlayerBaseActivity {
    private TextView title;
    Context context = this;
    RecyclerView binaural_recyclerView;
    BinauralAdapter binauralAdapter;
    ArrayList<songsPojo> binaural_beat_list = new ArrayList<>();
    private ImageView back;
    private int count_request_popup = 0;
    boolean isDownloadComplete;
    Uri nature_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binatural_beat);
        findId();
        binauralApi();
    }

    private void binauralApi() {

        try {
            Utility utility = Utility.getInstance(context);
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
            Call<RestResponse<BinauralPojo>> callApi = apiInterface.binauralEffect();
            callApi.enqueue(new Callback<RestResponse<BinauralPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<BinauralPojo>> call, Response<RestResponse<BinauralPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        binaural_beat_list.addAll(response.body().getData().getEffect_songs());

                        if (binaural_beat_list.size() > 0) {
                            setAdapter(binaural_beat_list);
                            binauralAdapter.BinarialInterfaceClick(new BinauralAdapter.BinauralClick() {
                                @Override
                                public void onClick(String id, String pos, String url, String desc, String title) {
                                    if (checkPermissionn()) {
                                        if (isDownloadComplete) {

                                            Toast.makeText(BinaturalBeatActivity.this, getResources().getString(R.string.download_inProgress), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        isDownloadComplete = true;

                                        downloadFile(url, desc, title);


                                    }
                                }
                            });
                        }


                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<BinauralPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setAdapter(ArrayList<songsPojo> binaural_beat_list) {
        binauralAdapter = new BinauralAdapter(context, binaural_beat_list);
        binaural_recyclerView.setAdapter(binauralAdapter);

    }

    private void findId() {
        binaural_recyclerView = findViewById(R.id.binaural_recyclerView);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        title.setText("Binaural Beats");
        binaural_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binaural_recyclerView.setHasFixedSize(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
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