package com.musicapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.musicapp.R;
import com.musicapp.activities.AudioPlayerViewActivity;
import com.musicapp.activities.DashboardActivity;
import com.musicapp.activities.SplashActivity;
import com.musicapp.models.songsPojo;
import com.musicapp.service.SoundService;

import java.util.ArrayList;

public class PlayerUtil {

    private static PlayerUtil playerUtil;
    private static Context sContext;
    private static AudioPlayerViewActivity mActivity;
    public SimpleExoPlayer simpleExoPlayer;
    private static String audio_Url;
    private com.musicapp.models.songsPojo songsPojo;
    ArrayList<songsPojo> list = new ArrayList<>();
    CountDownTimer countDownTimer;


    public com.musicapp.models.songsPojo getSongsPojo() {
        return songsPojo;
    }

    public void setSongsPojo(com.musicapp.models.songsPojo songsPojo) {
        this.songsPojo = songsPojo;
    }

    private PlayerUtil() {
        setPlayer();


    }


    public static PlayerUtil getInstance(Context context, String audio_Url1) {
        sContext = context;


        if (audio_Url1 == null)
            return playerUtil;
        audio_Url = audio_Url1;
        if (playerUtil == null) {
            playerUtil = new PlayerUtil();


        }

        return playerUtil;
    }

    public static void clearInstance() {
        sContext = null;
        audio_Url = null;
        playerUtil = null;
    }

    public void setAudio_Url(String audioUrl) {
        audio_Url = audioUrl;
    }

    public void setSongObject(com.musicapp.models.songsPojo songObject) {

        songsPojo = songObject;
    }

    PlayerListener playerListener;

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public void setPlayerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    public void setPlayer() {
        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(sContext, trackSelector, loadControl);

        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audio_Url), factory, extractorsFactory, null, null);


        simpleExoPlayer.prepare(mediaSource);


        //  simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);


        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                Log.e("Player_state", String.valueOf(playbackState));

                if (playerListener != null) {
                    playerListener.onPlayerStateChanged(playWhenReady, playbackState);
                }

                if (playbackState == Player.STATE_BUFFERING) {


                } else if (playbackState == Player.STATE_READY) {

                    // progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(sContext, SoundService.class);
                    intent.setAction(MusicConstants.ACTION.START_ACTION);
                    sContext.startService(intent);


                } else if (playbackState == Player.STATE_IDLE) {

                    Intent intent = new Intent(sContext, SoundService.class);
                    intent.setAction(MusicConstants.ACTION.PAUSE_ACTION);
                    sContext.startService(intent);


                } else if (playbackState == Player.STATE_ENDED) {

                    Intent intent = new Intent(sContext, SoundService.class);
                    intent.setAction(MusicConstants.ACTION.STOP_ACTION);
                    sContext.startService(intent);

                    if (list != null && list.size() != 0) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (mActivity.selected_index < list.size()) {

                                    Log.e("next_song", "next_song");
                                    //mActivity.selected_index = 1;
                                    mActivity.setSongs(list.get(mActivity.selected_index));
                                    mActivity.selected_index = mActivity.selected_index + 1;


                                }

                            }
                        }, 7000);
                    }


                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {


            }
        });
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (playerListener != null) {
                    playerListener.onSeekProcessed();
                }
                handler.removeCallbacks(this::run);
                handler.postDelayed(this::run, 1000);

            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public interface PlayerListener {
        public void onSeekProcessed();

        public void onPlayerStateChanged(boolean playWhenReady, int playbackState);
    }

    public void stopPlayer() {
        try {

//            if (mActivity != null) {
//                if (mActivity.countDownTimer != null) {
//                    mActivity.countDownTimer.cancel();
//
//                }
//            }


            if (countDownTimer != null) {
                countDownTimer.cancel();
            }


            if (simpleExoPlayer != null) {
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.getPlaybackState();
                Intent intent = new Intent(sContext, SoundService.class);
                intent.setAction(MusicConstants.ACTION.STOP_ACTION);
                sContext.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContextParams(ArrayList<com.musicapp.models.songsPojo> list, AudioPlayerViewActivity ativity) {
        mActivity = ativity;
        this.list = list;

    }

    public void fadeSong() {


        countDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("STATUS", "COUNTER");
                //here you can have your logic to set text to edittext
                int progress = (int) (millisUntilFinished / 1000);
                PlayerUtil playerUtil = PlayerUtil.getInstance(sContext, "");
                simpleExoPlayer = playerUtil.simpleExoPlayer;
                AudioManager audioManager = (AudioManager) sContext.getSystemService(Context.AUDIO_SERVICE);
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
                //Toast.makeText(sContext, "Done!", Toast.LENGTH_SHORT).show();
                PlayerUtil.getInstance(sContext, null).stopPlayer();
                mActivity.finish();
            }
        }.start();
    }


}
