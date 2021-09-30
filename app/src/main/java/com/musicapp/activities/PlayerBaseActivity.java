package com.musicapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.musicapp.R;
import com.musicapp.models.songsPojo;
import com.musicapp.util.Constants;
import com.musicapp.util.PlayerUtil;

public class PlayerBaseActivity extends AppCompatActivity {


    private LinearLayout linearLayout;

    @Override
    protected void onResume() {
        super.onResume();
        linearLayout = (LinearLayout)findViewById(R.id.playerLayout);

        PlayerUtil playerUtil = PlayerUtil.getInstance(this,null);
        if(playerUtil != null){
            setPlayer(playerUtil.getSongsPojo());
        }else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   setContentView(R.layout.bottom_song_layout);


    }


    public boolean loadFragment(Fragment fragment, int id, boolean isBackStack, String name) {
        //switching fragment
        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction
                    .replace(id, fragment)
                    .commit();

            if(isBackStack){
                fragmentTransaction.addToBackStack(name);
            }
            return true;
        }
        return false;
    }
    public void fullWindow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 909:
                if (resultCode == RESULT_OK){

                }
        }
    }

    public void setPlayer(songsPojo songsPojoObject){


        linearLayout.setVisibility(View.VISIBLE);
        PlayerUtil playerUtil = PlayerUtil.getInstance(this,songsPojoObject.getAudios());

        playerUtil.setPlayerListener(new PlayerUtil.PlayerListener() {
            @Override
            public void onSeekProcessed() {
              //  seekBar.setProgress((int) playerUtil.simpleExoPlayer.getCurrentPosition()/1000);

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }
        });



        ImageView playImage = (ImageView)findViewById(R.id.play);
        ImageView crossImage = (ImageView)findViewById(R.id.cross);
        TextView songName = (TextView) findViewById(R.id.song_name1);
        TextView songDuration = (TextView) findViewById(R.id.song_duration);
        TextView authorName = (TextView) findViewById(R.id.author_name);
        ImageView songCoverPIc = (ImageView) findViewById(R.id.song_cover_pic);

        songName.setText(songsPojoObject.getName());
        authorName.setText(songsPojoObject.getAuthor_name());
        songDuration.setText(songsPojoObject.getSongs_length());

//        seekBar.setMax((int) playerUtil.simpleExoPlayer.getDuration());
//        seekBar.setProgress((int) playerUtil.simpleExoPlayer.getCurrentPosition());

        Glide.with(this).load(songsPojoObject.getCover_images()).into(songCoverPIc);

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerUtil.stopPlayer();
                PlayerUtil.clearInstance();
                linearLayout.setVisibility(View.GONE);

            }
        });

        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerUtil.simpleExoPlayer.getPlayWhenReady()){
                    playImage.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    playerUtil.simpleExoPlayer.setPlayWhenReady(false);
                }else {
                    playImage.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_pause));

                    playerUtil.simpleExoPlayer.setPlayWhenReady(true);
                }
            }
        });
        if(playerUtil.simpleExoPlayer.getPlayWhenReady()){
            playImage.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_pause));
        }else {
            playImage.setImageDrawable(getResources().getDrawable(R.drawable.play));

        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerBaseActivity.this, AudioPlayerViewActivity.class);
                intent.putExtra(Constants.SONG_ID,playerUtil.getSongsPojo().getId());
                intent.putExtra(Constants.SONG_NAME,playerUtil.getSongsPojo().getTitle());
                intent.putExtra(Constants.SONG_DESCRIPTION,playerUtil.getSongsPojo().getDescription());
                intent.putExtra(Constants.SONG_COVER_IMAGE,playerUtil.getSongsPojo().getCover_images());
                intent.putExtra(Constants.SONG_AUDIO_URL,playerUtil.getSongsPojo().getAudios());
                intent.putExtra(Constants.SONG_AUTHOR_NAME,playerUtil.getSongsPojo().getAuthor_name());
                intent.putExtra(Constants.SONG_AUTHOR_IMAGE,playerUtil.getSongsPojo().getAuthor_image());

               startActivityForResult(intent,909);
            }
        });
    }

}
