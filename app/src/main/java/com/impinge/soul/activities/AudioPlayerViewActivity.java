package com.impinge.soul.activities;

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
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.impinge.soul.R;
import com.impinge.soul.localdatabase.DatabaseClient;
import com.impinge.soul.localdatabase.DownloadedSongsModel;
import com.impinge.soul.models.DataPojo;
import com.impinge.soul.models.User;
import com.impinge.soul.models.songsPojo;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.service.SoundService;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.MusicConstants;
import com.impinge.soul.util.PlayerUtil;
import com.impinge.soul.util.PreferenceData;
import com.impinge.soul.util.SharedPreference;
import com.impinge.soul.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;

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
    ArrayList<songsPojo> list = new ArrayList<>();
    public int selected_index = 1;
    public static CountDownTimer countDownTimer;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.e("getAction", "intentttttt" + intent.getAction());

        switch (intent.getAction()) {

            case MusicConstants.ACTION.PAUSE_ACTION:

                Log.e("getAction", "PAUSE");
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.getPlaybackState();
                Intent intent1 = new Intent(AudioPlayerViewActivity.this, SoundService.class);
                intent1.setAction(MusicConstants.ACTION.PAUSE_ACTION);
                startService(intent1);
                break;

            case MusicConstants.ACTION.PLAY_ACTION:
                Log.e("getAction", "PLAY");
                simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer.getPlaybackState();
                Intent intent2 = new Intent(AudioPlayerViewActivity.this, SoundService.class);
                intent2.setAction(MusicConstants.ACTION.START_ACTION);
                startService(intent2);
                break;

            case MusicConstants.ACTION.STOP_ACTION:

                Log.e("getAction", "STOP");
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.getPlaybackState();
                Intent intent3 = new Intent(AudioPlayerViewActivity.this, SoundService.class);
                intent3.setAction(MusicConstants.ACTION.STOP_ACTION);
                startService(intent3);
                break;

            case "stop":
                //fadeSong();
                PlayerUtil playerUtil = PlayerUtil.getInstance(this, "");
                playerUtil.fadeSong();

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

        Log.e("check_intent", "onCreate");

        if (getIntent().hasExtra("music_alarm_notification")) {
            String title = getIntent().getStringExtra("title");
            String album_id = getIntent().getStringExtra("album_id");
            String subtype = getIntent().getStringExtra("subtype");

            if (subtype.equalsIgnoreCase("music_alarm_notification_start")) {
                getAllSongs(album_id);
                Log.e("STATUS_TYPE", "START");

            } else if (subtype.equalsIgnoreCase("music_alarm_notification_end")) {
                Log.e("STATUS_TYPE", "STOP");

                //fadeSong();

                PlayerUtil playerUtil = PlayerUtil.getInstance(this, null);
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


                playerView.setPlayer(playerUtil.simpleExoPlayer);
                playerView.setKeepScreenOn(true);
                playerView.setControllerAutoShow(true);
                //  playerUtil.simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer = playerUtil.simpleExoPlayer;


                mIvEqualizer = (ImageView) playerView.findViewById(R.id.equalizer);
                mIvStop = (ImageView) playerView.findViewById(R.id.stop);
                mIvStop.setOnClickListener(this);
                mIvEqualizer.setOnClickListener(this);
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

//                playerUtil.stopPlayer();

                //fadeSong();
                PlayerUtil playerUtil1 = PlayerUtil.getInstance(this, "");
                playerUtil1.fadeSong();
            }


        } else if (getIntent().hasExtra("music_duration_notification")) {

            String album_id = getIntent().getStringExtra("album_id");
            String title = getIntent().getStringExtra("title");

            if (title.equalsIgnoreCase("Alarm Duration Notification")) {


                PlayerUtil playerUtil = PlayerUtil.getInstance(this, null);
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


                playerView.setPlayer(playerUtil.simpleExoPlayer);
                playerView.setKeepScreenOn(true);
                playerView.setControllerAutoShow(true);
                //  playerUtil.simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayer = playerUtil.simpleExoPlayer;


                mIvEqualizer = (ImageView) playerView.findViewById(R.id.equalizer);
                mIvStop = (ImageView) playerView.findViewById(R.id.stop);
                mIvStop.setOnClickListener(this);
                mIvEqualizer.setOnClickListener(this);
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

//                playerUtil.stopPlayer();

                //fadeSong();
                PlayerUtil playerUtil1 = PlayerUtil.getInstance(this, "");
                playerUtil1.fadeSong();
            } else {

                // getAllSongs(album_id);
            }


        } else if (getIntent().hasExtra("duration")) {

            int album_id = getIntent().getIntExtra("duration_category", 0);
            Log.e("coming_from", "setDuration--------" + " " + album_id);

            getAllSongs(String.valueOf(album_id));
        } else {

            playNormalSong();
        }
    }

    private void playNormalSong() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(perms, permsRequestCode);
//        }

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
        boolean IS_PAUSE = getIntent().getBooleanExtra(Constants.IS_PAUSE,true);

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

        playerView.setPlayer(playerUtil.simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        playerView.setControllerAutoShow(true);

        playerUtil.simpleExoPlayer.setPlayWhenReady(IS_PAUSE);
        simpleExoPlayer = playerUtil.simpleExoPlayer;

        mIvEqualizer = (ImageView) playerView.findViewById(R.id.equalizer);
        mIvEqualizer.setOnClickListener(this::onClick);
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

        if (getIntent().hasExtra("adapter")) {
            songDetailApi();
            Log.e("adapter", "adapter");
        }
    }

    public void fadeSong() {


        countDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("STATUS", "COUNTER");
                //here you can have your logic to set text to edittext
                int progress = (int) (millisUntilFinished / 1000);
                PlayerUtil playerUtil = PlayerUtil.getInstance(AudioPlayerViewActivity.this, "");
                simpleExoPlayer = playerUtil.simpleExoPlayer;
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int vol = audioManager.getStreamVolume(simpleExoPlayer.getAudioStreamType());
                Log.e("progress", String.valueOf(progress));
                Log.e("volume", String.valueOf(vol));
                //  int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float percent = 0.7f;
                int seventyVolume = (int) (maxVolume * percent);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

            }

            public void onFinish() {
                Toast.makeText(AudioPlayerViewActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                PlayerUtil.getInstance(AudioPlayerViewActivity.this, null).stopPlayer();
                finish();
            }
        }.start();
//        new CountDownTimer(15000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                Log.e("STATUS", "COUNTER");
//                //here you can have your logic to set text to edittext
//                int progress = (int) (millisUntilFinished / 1000);
//                PlayerUtil playerUtil = PlayerUtil.getInstance(AudioPlayerViewActivity.this, "");
//                simpleExoPlayer = playerUtil.simpleExoPlayer;
//                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                int vol = audioManager.getStreamVolume(simpleExoPlayer.getAudioStreamType());
//                Log.e("progress", String.valueOf(progress));
//                Log.e("volume", String.valueOf(vol));
//                //  int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//                float percent = 0.7f;
//                int seventyVolume = (int) (maxVolume * percent);
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//
//            }
//
//            public void onFinish() {
//                Toast.makeText(AudioPlayerViewActivity.this, "Done!", Toast.LENGTH_SHORT).show();
//                PlayerUtil.getInstance(AudioPlayerViewActivity.this, null).stopPlayer();
//            }
//
//        }.start();
    }


    private void getAllSongs(String album_id) {

        try {
            Utility utility = Utility.getInstance(this);

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Log.e("parametrs_album", album_id);
            Call<RestResponse<DataPojo>> callApi = apiInterface.getSongList(Integer.parseInt(album_id));
            callApi.enqueue(new Callback<RestResponse<DataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<DataPojo>> call, Response<RestResponse<DataPojo>> response) {
                    utility.hideLoading();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(perms, permsRequestCode);
                    }


                    Log.e("response_msg", String.valueOf(response.code()));

                    if (response.code() == 200) {

                        DataPojo dataPojo = response.body().data();

                        list.addAll(dataPojo.getCategoryPojoArrayList().get(0).getSongs());


                        setSongs(list.get(0));


                    } else {
                        Toast.makeText(AudioPlayerViewActivity.this, response.message(), Toast.LENGTH_SHORT).show();

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


    public void setSongs(songsPojo songsPojo) {
        try {
            PlayerUtil playerUtil = PlayerUtil.getInstance(this, songsPojo.getAudios());
            playerUtil.stopPlayer();
            playerUtil.clearInstance();
            playerUtil = PlayerUtil.getInstance(this, songsPojo.getAudios());
            playerUtil.setSongsPojo(songsPojo);

            Log.e("selected_index", String.valueOf(selected_index));

            playerUtil.setContextParams(list, this);

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

            if (playerView == null) {
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

            }


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


            playerView.setPlayer(playerUtil.simpleExoPlayer);
            playerView.setKeepScreenOn(true);
            playerView.setControllerAutoShow(true);
            playerUtil.simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer = playerUtil.simpleExoPlayer;


            // TODO: 16-09-2021 this 2 line code is added whenuser come in this screen audio will be full by default

            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);

            // TODO: 16-09-2021 this 2 line code is added whenuser come in this screen audio will be full by default


            mIvEqualizer = (ImageView) playerView.findViewById(R.id.equalizer);
            mIvStop = (ImageView) playerView.findViewById(R.id.stop);
            mIvStop.setOnClickListener(this);
            mIvEqualizer.setOnClickListener(this);
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


        } catch (Exception e) {
            e.printStackTrace();

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

                    if (permission1 ||  permission2) {
                        // Snackbar.make(mRvMainLayout, "Permission Granted, Now you can access Storage.", Snackbar.LENGTH_LONG).show();
                    } else {

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

        Log.e("onDestroy", "Audio_player");

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
                PlayerUtil playerUtil = PlayerUtil.getInstance(this, null);
                playerUtil.stopPlayer();
                simpleExoPlayer.seekTo(0);


                break;

            case R.id.equalizer:
                Toast.makeText(this,getResources().getString(R.string.comming_soon),Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(AudioPlayerViewActivity.this, getResources().getString(R.string.fav_song_added), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(AudioPlayerViewActivity.this,  getResources().getString(R.string.fav_song_remove), Toast.LENGTH_SHORT).show();
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