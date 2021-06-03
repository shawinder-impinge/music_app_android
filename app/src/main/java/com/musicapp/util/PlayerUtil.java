package com.musicapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
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
import com.musicapp.service.SoundService;

public class PlayerUtil {

    private static PlayerUtil playerUtil;
    private static Context sContext;
    public SimpleExoPlayer simpleExoPlayer;
    private static String audio_Url;
    private com.musicapp.models.songsPojo songsPojo;

    public com.musicapp.models.songsPojo getSongsPojo() {
        return songsPojo;
    }

    public void setSongsPojo(com.musicapp.models.songsPojo songsPojo) {
        this.songsPojo = songsPojo;
    }

    private PlayerUtil(){
        setPlayer();




    }





    public static PlayerUtil getInstance(Context context, String audio_Url1){
        sContext = context;

        if(audio_Url1 == null)
            return playerUtil ;
        audio_Url = audio_Url1;
        if(playerUtil == null){
            playerUtil = new PlayerUtil();


        }

        return playerUtil;
    }

    public static void clearInstance(){
        sContext = null;
        audio_Url = null;
        playerUtil = null;
    }
    public  void setAudio_Url(String audioUrl){
        audio_Url = audioUrl;
    }

    public void setSongObject(com.musicapp.models.songsPojo songObject){

        songsPojo = songObject;
    }
    PlayerListener playerListener;

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public void setPlayerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    public  void setPlayer(){
        LoadControl loadControl= new DefaultLoadControl();

        BandwidthMeter bandwidthMeter=  new DefaultBandwidthMeter();

        TrackSelector trackSelector= new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(sContext,trackSelector,loadControl);

        DefaultHttpDataSourceFactory factory= new DefaultHttpDataSourceFactory("exoplayer_video");

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource= new ExtractorMediaSource(Uri.parse(audio_Url),factory,extractorsFactory,null,null);


        simpleExoPlayer.prepare(mediaSource);


        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);




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
                if(playerListener !=null) {
                    playerListener.onPlayerStateChanged(playWhenReady, playbackState);
                }

                if (playbackState == Player.STATE_BUFFERING) {


                } else if (playbackState == Player.STATE_READY) {

                   // progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(sContext, SoundService.class);
                    intent.setAction(MusicConstants.ACTION.START_ACTION);
                    sContext.startService(intent);


                }

                else if(playbackState == Player.STATE_IDLE){

                    Intent intent = new Intent(sContext, SoundService.class);
                    intent.setAction(MusicConstants.ACTION.PAUSE_ACTION);
                    sContext.startService(intent);

                }

                else if(playbackState == Player.STATE_ENDED){

                    Intent intent = new Intent(sContext, SoundService.class);
                    intent.setAction(MusicConstants.ACTION.STOP_ACTION);
                    sContext.startService(intent);

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
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                if(playerListener!= null) {
                    playerListener.onSeekProcessed();
                }
                handler.removeCallbacks(this::run);
                handler.postDelayed(this::run,1000);

            }
        };
       handler.postDelayed(runnable,1000);
    }

    public interface PlayerListener{
        public void onSeekProcessed();
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState);
    }

    public void stopPlayer(){
        try {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.getPlaybackState();
                Intent intent = new Intent(sContext, SoundService.class);
                intent.setAction(MusicConstants.ACTION.STOP_ACTION);
                sContext.startService(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
