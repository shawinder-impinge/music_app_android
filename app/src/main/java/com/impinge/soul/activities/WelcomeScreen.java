package com.impinge.soul.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.impinge.soul.R;
import com.impinge.soul.models.LoginVideoModel;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WelcomeScreen extends BaseActivity implements View.OnClickListener {
    public TextView skip, lets_start;
    String video_link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        fullWindow();
        findId();

        //   getLoginVideo();
        // mixaudioFiles();
    }

    private void getLoginVideo() {

        try {
            Utility utility = Utility.getInstance(this);
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Call<RestResponse<LoginVideoModel>> callApi = apiInterface.getLoginVideo();
            callApi.enqueue(new Callback<RestResponse<LoginVideoModel>>() {
                @Override
                public void onResponse(Call<RestResponse<LoginVideoModel>> call, Response<RestResponse<LoginVideoModel>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        video_link = response.body().getData().getVideo().getVideo();

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<LoginVideoModel>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

//    private void mixaudioFiles() {
//        if (FFmpeg.getInstance(this).isSupported()) {
//            // ffmpeg is supported
//        } else {
//            // ffmpeg is not supported
//        }
//
//        String path = Environment.getExternalStorageDirectory().getPath()+"/downloads/Ishq_Forever.mp3";
//        String path1 = Environment.getExternalStorageDirectory().getPath()+"/downloads/Daddy.mp3";
//
//        File file = new File(Environment.getExternalStorageDirectory()+"/abcd.mp3");
//
//        String path2  = file.getPath();
//        String query1 = "-i /storage/emulated/0/Download/Daddy.mp3 -i /storage/emulated/0/Download/Ishq.mp3  -filter_complex amix=inputs=1:duration=first:dropout_transition=0 -codec:a libmp3lame -q:a   /storage/emulated/0/Download/Ishq.mp3";
//        String query = "-i /storage/emulated/0/Download/Daddy.mp3 -filter:a volume=0.5 /storage/emulated/0/Download/Ishq.mp3";
//        String[] commands = query.split(" ");
//        FFtask rc = FFmpeg.getInstance(this).execute(commands, new FFcommandExecuteResponseHandler() {
//            @Override
//            public void onSuccess(String message) {
//
//            }
//
//            @Override
//            public void onProgress(String message) {
//
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
//    }


    private void findId() {
        skip = findViewById(R.id.skip);
        lets_start = findViewById(R.id.lets_start);
        skip.setOnClickListener(this);
        lets_start.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip:
                redirectSignUp();
                break;

            case R.id.lets_start:
                redirectSignUp();
                break;
        }
    }

    private void redirectSignUp() {
        startActivity(new Intent(WelcomeScreen.this, SignUpActivity.class).putExtra("link", video_link));
        overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

    }

    @Override
    protected void onResume() {
        super.onResume();
       // getLoginVideo();
    }

}