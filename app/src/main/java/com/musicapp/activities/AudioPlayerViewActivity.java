package com.musicapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.snackbar.Snackbar;
import com.musicapp.R;
import com.musicapp.localdatabase.DatabaseClient;
import com.musicapp.localdatabase.DownloadedSongsModel;
import com.musicapp.models.User;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.service.SoundService;
import com.musicapp.util.MusicConstants;
import com.musicapp.util.PlayerUtil;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioPlayerViewActivity extends AppCompatActivity implements View.OnClickListener {

    PlayerView playerView;
    ProgressBar progressBar;

    ImageView mIvEqualizer;
    ImageView mIvStop;
    ImageView mIvDownload;
    SimpleExoPlayer simpleExoPlayer;
    ImageView mIvRewind, mIvForward;
    private Uri audioUrl;
    int permsRequestCode = 200;
    RelativeLayout mRvMainLayout;

    boolean isDownloadComplete;
    TextView mTvAlbumName;

    String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    private int songId;
    private String songTitle, songDescription, song_coverImage, song_duration;
    private TextView mTvSongName;
    private String albumTitle;
    private static String song_audio_url;
    private String song_author_name, song_author_image;
    private TextView mTvAuthorName;
    private ImageView mIvAuthorImage, drop_down;
    private ImageView mIvShare, song_like;
    boolean isUserFav;
    private songsPojo songsPojoObject;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        switch (intent.getAction()) {
            case MusicConstants.ACTION.PAUSE_ACTION:
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.getPlaybackState();
                Intent intent1 = new Intent(AudioPlayerViewActivity.this, SoundService.class);
                intent1.setAction(MusicConstants.ACTION.PAUSE_ACTION);
                startService(intent1);
                break;

            case MusicConstants.ACTION.PLAY_ACTION:

                simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer.getPlaybackState();
                Intent intent2 = new Intent(AudioPlayerViewActivity.this, SoundService.class);
                intent2.setAction(MusicConstants.ACTION.START_ACTION);
                startService(intent2);
                break;

            case MusicConstants.ACTION.STOP_ACTION:
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.getPlaybackState();
                Intent intent3 = new Intent(AudioPlayerViewActivity.this, SoundService.class);
                intent3.setAction(MusicConstants.ACTION.STOP_ACTION);
                startService(intent3);
                break;
        }
    }


    private void DownloadSongToLocal(String title, String path, String image, String duration, int song_id, String song_desc, String author_name, String author_image, String song_url) {
        class SaveTask extends AsyncTask<String, Void, Void> {


            @Override
            protected Void doInBackground(String... details) {

                //creating a task
                DownloadedSongsModel model = new DownloadedSongsModel();
                model.setTask(details[0]);
                model.setName(details[1]);
                model.setImage(details[2]);
                model.setDuration(details[3]);
                model.setSongid(Integer.parseInt(details[4]));
                model.setSong_desc(details[5]);
                model.setAuthor_name(details[6]);
                model.setAuthor_image(details[7]);
                model.setSong_url(details[8]);


                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(model);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Your song has been downloaded successfully", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute(path, title, image, duration, String.valueOf(song_id), song_desc, author_name, author_image, song_url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_view);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }

        PlayerUtil playerUtil = PlayerUtil.getInstance(this, song_audio_url);


        songsPojoObject = playerUtil.getSongsPojo();
        songId = songsPojoObject.getId();
        songTitle = songsPojoObject.getTitle();
        // albumTitle = getIntent().getStringExtra(Constants.ALBUM_TITILE);
        songDescription = songsPojoObject.getDescription();
        song_coverImage = songsPojoObject.getCover_images();
        song_duration = songsPojoObject.getSongs_length();
        song_audio_url = songsPojoObject.getAudios();
        song_author_name = songsPojoObject.getAuthor_name();
        song_author_image = songsPojoObject.getAuthor_image();

        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        mIvDownload = findViewById(R.id.download);
        mRvMainLayout = findViewById(R.id.main_layout);
        mTvAlbumName = findViewById(R.id.album_name);
        mTvSongName = findViewById(R.id.song_name);
        mTvAuthorName = findViewById(R.id.author_name);
        mIvAuthorImage = findViewById(R.id.author_image);
        drop_down = findViewById(R.id.drop_down);
        drop_down.setOnClickListener(this);

        mTvSongName.setText(songTitle);
        mTvAlbumName.setText(songTitle);
        mTvAuthorName.setText(song_author_name);

        Glide.with(this).load(song_coverImage).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRvMainLayout.setBackground(resource);
                }
            }
        });

        Glide.with(this).load(song_author_image).into(mIvAuthorImage);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //     audioUrl= Uri.parse(song_audio_url);

        playerUtil.setPlayerListener(new PlayerUtil.PlayerListener() {
            @Override
            public void onSeekProcessed() {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {

                    progressBar.setVisibility(View.VISIBLE);

                } else if (playbackState == Player.STATE_READY) {

                    progressBar.setVisibility(View.GONE);


                }
            }
        });
        //  playerUtil.setAudio_Url(song_audio_url);

        playerView.setPlayer(playerUtil.simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        playerUtil.simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer = playerUtil.simpleExoPlayer;

//        LoadControl loadControl= new DefaultLoadControl();
//
//        BandwidthMeter bandwidthMeter=  new DefaultBandwidthMeter();
//
//        TrackSelector trackSelector= new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//
//        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(AudioPlayerViewActivity.this,trackSelector,loadControl);
//
//        DefaultHttpDataSourceFactory factory= new DefaultHttpDataSourceFactory("exoplayer_video");
//
//        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
//        MediaSource mediaSource= new ExtractorMediaSource(audioUrl,factory,extractorsFactory,null,null);
//
//        playerView.setPlayer(simpleExoPlayer);
//        playerView.setKeepScreenOn(true);
//        simpleExoPlayer.prepare(mediaSource);
//        simpleExoPlayer.setPlayWhenReady(true);
//
//
//        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);


        mIvEqualizer = (ImageView) playerView.findViewById(R.id.equalizer);
        mIvStop = (ImageView) playerView.findViewById(R.id.stop);
        mIvStop.setOnClickListener(this);
        mIvShare = (ImageView) findViewById(R.id.share);
        song_like = (ImageView) findViewById(R.id.song_like);
        mIvShare.setOnClickListener(this);
        mIvDownload.setOnClickListener(this);
        song_like.setOnClickListener(this);


        if (songsPojoObject.isIs_user_fav()) {
            song_like.setImageDrawable(AudioPlayerViewActivity.this.getResources().getDrawable(R.drawable.music_like));

        } else {
            song_like.setImageDrawable(AudioPlayerViewActivity.this.getResources().getDrawable(R.drawable.like));

        }


//        simpleExoPlayer.addListener(new Player.EventListener() {
//            @Override
//            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//            }
//
//            @Override
//            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//            }
//            @Override
//            public void onLoadingChanged(boolean isLoading) {
//
//            }
//
//            @Override
//            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//
//                if (playbackState == Player.STATE_BUFFERING) {
//
//                    progressBar.setVisibility(View.VISIBLE);
//
//                } else if (playbackState == Player.STATE_READY) {
//
//                    progressBar.setVisibility(View.GONE);
//
//                    Intent intent = new Intent(AudioPlayerViewActivity.this,SoundService.class);
//                    intent.setAction(MusicConstants.ACTION.START_ACTION);
//                    startService(intent);
//
//                }
//
//                else if(playbackState == Player.STATE_IDLE){
//
//                    Intent intent = new Intent(AudioPlayerViewActivity.this,SoundService.class);
//                    intent.setAction(MusicConstants.ACTION.PAUSE_ACTION);
//                    startService(intent);
//
//                }
//
//                else if(playbackState == Player.STATE_ENDED){
//
//                    Intent intent = new Intent(AudioPlayerViewActivity.this,SoundService.class);
//                    intent.setAction(MusicConstants.ACTION.STOP_ACTION);
//                    startService(intent);
//
//                }
//            }
//                @Override
//                public void onRepeatModeChanged(int repeatMode) {
//
//                }
//
//                @Override
//                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//                }
//
//                @Override
//                public void onPlayerError(ExoPlaybackException error) {
//
//                }
//
//                @Override
//                public void onPositionDiscontinuity(int reason) {
//
//                }
//
//                @Override
//                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//                }
//
//                @Override
//                public void onSeekProcessed() {
//
//                }
//            });

        if (getIntent().hasExtra("adapter")) {
            songDetailApi();
            Log.e("adapter", "adapter");
        }
    }


    private void songDetailApi() {
        Log.e("RESPONSE", "songDetailApi");

        try {
            Utility utility = Utility.getInstance(this);

            //  utility.showLoading(sContext.getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Log.e("RESPONSE", String.valueOf(songId));

            Call<RestResponse<User>> callApi = apiInterface.songDetail(songId);
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {
                    // utility.hideLoading();

                    Log.e("RESPONSE", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        Log.e("RESPONSE", "SUCCESS");

                    } else {
                        Log.e("RESPONSE", "FAILURE");

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    // utility.hideLoading();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

//            simpleExoPlayer.setPlayWhenReady(false);
//            simpleExoPlayer.getPlaybackState();

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, perms, permsRequestCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0) {

                    boolean permission1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (permission1 && permission2)

                        Snackbar.make(mRvMainLayout, "Permission Granted, Now you can access Storage.", Snackbar.LENGTH_LONG).show();
                    else {

                        //  requestPermission();
                        Snackbar.make(mRvMainLayout, "Permission Denied, You cannot access Storage.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(perms,
                                                            permsRequestCode);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AudioPlayerViewActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//            simpleExoPlayer.setPlayWhenReady(true);
//            simpleExoPlayer.getPlaybackState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        simpleExoPlayer.setPlayWhenReady(false);
//        simpleExoPlayer.getPlaybackState();
//        Intent intent = new Intent(AudioPlayerViewActivity.this,SoundService.class);
//        intent.setAction(MusicConstants.ACTION.STOP_ACTION);
//        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop:
                simpleExoPlayer.seekTo(0);

                break;
            case R.id.share:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                String app_url = " https://play.google.com/store/apps/details?id=my.example.javatpoint";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, songsPojoObject.getAudios());
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.song_like:
                calFavouriteApi();

                break;

            case R.id.drop_down:
                onBackPressed();

                break;

            case R.id.download:

                if (isDownloadComplete) {

                    Toast.makeText(this, getResources().getString(R.string.download_inProgress), Toast.LENGTH_SHORT).show();
                    return;
                }
                isDownloadComplete = true;
                if (!checkPermission()) {

                    requestPermission();

                } else {

                    downloadFile();

                }

                break;
        }
    }

    private void calFavouriteApi() {
        Utility utility = Utility.getInstance(this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", SharedPreference.fetchPrefenceData(this, PreferenceData.USER_ID));
        hashMap.put("songs", songId);

        Log.e("SIGNUP_DATA", String.valueOf(hashMap));
        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);


        if (!songsPojoObject.isIs_user_fav()) {
            Call<RestResponse<User>> callApi = apiInterface.addToFavourite(hashMap);
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        songsPojoObject.setIs_user_fav(true);
                        song_like.setImageDrawable(AudioPlayerViewActivity.this.getResources().getDrawable(R.drawable.music_like));

                        Toast.makeText(AudioPlayerViewActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());

                }
            });
        } else {


            Call<RestResponse<User>> removecallApi = apiInterface.removeFromFavourite(hashMap);
            removecallApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {


                        utility.hideLoading();
                        songsPojoObject.setIs_user_fav(false);
                        song_like.setImageDrawable(AudioPlayerViewActivity.this.getResources().getDrawable(R.drawable.like));
                        Toast.makeText(AudioPlayerViewActivity.this, response.message(), Toast.LENGTH_SHORT).show();
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

    private void downloadFile() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(song_audio_url));
        request.setDescription(songsPojoObject.getDescription());
        request.setTitle(songsPojoObject.getTitle());
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, songsPojoObject.getTitle() + ".mp3");


// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(this.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            isDownloadComplete = false;
            DownloadSongToLocal(songTitle, Environment.DIRECTORY_DOWNLOADS + "/" + songsPojoObject.getTitle() + ".mp3", song_coverImage, song_duration, songId, songDescription, song_author_name, song_author_image, song_audio_url);

        }
    };


}