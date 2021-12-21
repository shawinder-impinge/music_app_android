package com.impinge.soul.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.impinge.soul.R;
import com.impinge.soul.models.songsPojo;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.PlayerUtil;

public class PlayerBaseActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private  ImageView playImage,crossImage;
    private  TextView songName,songDuration,authorName;
    private  ImageView  songCoverPIc;

    @Override
    protected void onResume() {
        super.onResume();

        if(linearLayout== null) {
            linearLayout = (LinearLayout) findViewById(R.id.playerLayout);
        }

        PlayerUtil playerUtil = PlayerUtil.getInstance(this,null);
        if(playerUtil != null){

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    setPlayer(playerUtil.getSongsPojo());

//                }
//            },100);
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

        songCoverPIc = (ImageView) linearLayout.findViewById(R.id.song_cover_pic);
        songCoverPIc.setImageBitmap(null);
        songCoverPIc.setImageURI(null);
        new LongOperation(this,songsPojoObject.getCover_images()).execute();

            playImage = (ImageView) linearLayout.findViewById(R.id.play);
            crossImage = (ImageView)linearLayout. findViewById(R.id.cross);
            songName = (TextView) linearLayout.findViewById(R.id.song_name1);
            songDuration = (TextView)linearLayout. findViewById(R.id.song_duration);
            authorName = (TextView) linearLayout.findViewById(R.id.author_name);



            songName.setText(songsPojoObject.getName());
            authorName.setText(songsPojoObject.getAuthor_name());
            songDuration.setText(songsPojoObject.getSongs_length());

//        seekBar.setMax((int) playerUtil.simpleExoPlayer.getDuration());
//        seekBar.setProgress((int) playerUtil.simpleExoPlayer.getCurrentPosition());


//            RequestOptions myOptions = new RequestOptions()
//                    .override(50, 50);
//
//            Glide.with(PlayerBaseActivity.this)
//
//                    .load(songsPojoObject.getCover_images())
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                    .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
//                    .into(songCoverPIc);


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
                    if (playerUtil.simpleExoPlayer.getPlayWhenReady()) {
                        playImage.setImageDrawable(getResources().getDrawable(R.drawable.play));
                        playerUtil.simpleExoPlayer.setPlayWhenReady(false);
                    } else {
                        playImage.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_pause));

                        playerUtil.simpleExoPlayer.setPlayWhenReady(true);
                    }
                }
            });

            if (playerUtil.simpleExoPlayer.getPlayWhenReady()) {
                playImage.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_pause));
            } else {
                playImage.setImageDrawable(getResources().getDrawable(R.drawable.play));
            }

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayerBaseActivity.this, AudioPlayerViewActivity.class);
                    intent.putExtra(Constants.SONG_ID, playerUtil.getSongsPojo().getId());
                    intent.putExtra(Constants.SONG_NAME, playerUtil.getSongsPojo().getTitle());
                    intent.putExtra(Constants.SONG_DESCRIPTION, playerUtil.getSongsPojo().getDescription());
                    intent.putExtra(Constants.SONG_COVER_IMAGE, playerUtil.getSongsPojo().getCover_images());
                    intent.putExtra(Constants.SONG_AUDIO_URL, playerUtil.getSongsPojo().getAudios());
                    intent.putExtra(Constants.SONG_AUTHOR_NAME, playerUtil.getSongsPojo().getAuthor_name());
                    intent.putExtra(Constants.SONG_AUTHOR_IMAGE, playerUtil.getSongsPojo().getAuthor_image());
                    intent.putExtra(Constants.IS_PAUSE, playerUtil.simpleExoPlayer.getPlayWhenReady());

                    startActivityForResult(intent, 909);
                }
            });

       // Glide.with(this).clear(songCoverPIc);




//        Glide.with(PlayerBaseActivity.this)
//
//                .load(songsPojoObject.getCover_images())
//
//                .into(songCoverPIc);


    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {

        String url;
        Context context;
        public LongOperation( Context context,String url){
            this.url = url;
            this.context= context;
        }
        @Override
        protected String doInBackground(Void... params) {
                try {
                    Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        songCoverPIc.invalidate();

                                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

                                     //   Bitmap bitmap1 = getResizedBitmap(bitmap, 50);

                                        songCoverPIc.setImageBitmap(bitmap);




                                    }
                                });

                            }
                        }
                    });                } catch (Exception e) {
                    // We were cancelled; stop sleeping!
                }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
           // txt.setText(result);
            // You might want to change "executed" for the returned string
            // passed into onPostExecute(), but that is up to you
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            PlayerUtil playerUtil = PlayerUtil.getInstance(this, null);
//            if (playerUtil.simpleExoPlayer.getPlayWhenReady()) {
//                playImage.setImageDrawable(getResources().getDrawable(R.drawable.play));
//                playerUtil.simpleExoPlayer.setPlayWhenReady(false);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
