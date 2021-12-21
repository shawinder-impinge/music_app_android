package com.impinge.soul.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.impinge.soul.R;
import com.impinge.soul.adapter.SongListAdpter;
import com.impinge.soul.models.DataPojo;
import com.impinge.soul.models.songsPojo;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.Utility;
import com.impinge.soul.util.View.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumActivity extends PlayerBaseActivity {
    private static final int VERTICAL_ITEM_SPACE = 20;
    Context context = this;
    RecyclerView recyclerView;
    private SongListAdpter songListAdpter;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    boolean wasPlaying = false;
    private ArrayList<songsPojo> songsPojoarrayList = new ArrayList<>();
    private int albumId;
    TextView mTvTitle, toolbar_title;
    private String albumTitle, albumDexcription;
    private String albumImage;
    AppBarLayout appBarLayout;
    private String albumAuthorName, albumAuthorImage;
    TextView mTvAlbumAuthorName;
    CircleImageView mIvAlbumAuthorImage;
    LinearLayout adapter_lay, no_item_lay;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        if (getIntent() != null) {
            albumId = getIntent().getIntExtra(Constants.ALBUM_ID, 0);
            albumTitle = getIntent().getStringExtra(Constants.ALBUM_TITILE);
            albumDexcription = getIntent().getStringExtra(Constants.ALBUM_DESCRIPTION);
            albumImage = getIntent().getStringExtra(Constants.ALBUM_COVER_IMAGE);
            albumAuthorName = getIntent().getStringExtra(Constants.ALBUM_AUTHOR_NAME);
            albumAuthorImage = getIntent().getStringExtra(Constants.ALBUM_AUTHOR_IMAGE);

            Log.e("ALBUM_ID", String.valueOf(albumId));
        }

        findId();
        setRecycler();

        Glide.with(this).load(albumImage).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    appBarLayout.setBackground(resource);
                }
            }
        });

        Glide.with(this).load(albumAuthorImage).into(mIvAlbumAuthorImage);


        mTvTitle.setText(albumTitle);
        mTvAlbumAuthorName.setText(albumAuthorName);
        fetchAlbumSongs();

    }

    private void setRecycler() {

        if (songListAdpter == null) {
            songListAdpter = new SongListAdpter(AlbumActivity.this, songsPojoarrayList);
            recyclerView.setAdapter(songListAdpter);
        } else {
            songListAdpter.notifyDataSetChanged();
        }


    }

    private void fetchAlbumSongs() {

        try {
            Utility utility = Utility.getInstance(this);

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
            Call<RestResponse<DataPojo>> callApi = apiInterface.getSongList(albumId);
            callApi.enqueue(new Callback<RestResponse<DataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<DataPojo>> call, Response<RestResponse<DataPojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        DataPojo dataPojo = response.body().data();

                        if (dataPojo.getCategoryPojoArrayList() != null && dataPojo.getCategoryPojoArrayList().size() > 0) {
                            adapter_lay.setVisibility(View.VISIBLE);
                            no_item_lay.setVisibility(View.GONE);
                            songsPojoarrayList.addAll(dataPojo.getCategoryPojoArrayList().get(0).getSongs());
                            setRecycler();
                        } else {
                            adapter_lay.setVisibility(View.GONE);
                            no_item_lay.setVisibility(View.VISIBLE);
                        }


                       // Toast.makeText(AlbumActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AlbumActivity.this, response.message(), Toast.LENGTH_SHORT).show();

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


    public void playSong() {

        try {


            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                clearMediaPlayer();
                seekBar.setProgress(0);
                wasPlaying = true;
            }


            if (!wasPlaying) {

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }


                mediaPlayer.setDataSource(this, Uri.parse(APIClient.MEDIA_URL + "/media/audios/mixkit-island-beat-250.mp3"));


                mediaPlayer.prepare();
                mediaPlayer.setVolume(0.5f, 0.5f);
                mediaPlayer.setLooping(false);
                seekBar.setMax(mediaPlayer.getDuration());

                mediaPlayer.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        run1();
                    }
                }).start();

            }

            wasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void run1() {

        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();


        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            seekBar.setProgress(currentPosition);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    clearMediaPlayer();
    }

    private void clearMediaPlayer() {

    }

    private void setAdapter() {


        if (songListAdpter == null) {
            songListAdpter = new SongListAdpter(AlbumActivity.this, songsPojoarrayList);
            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

            recyclerView.setAdapter(songListAdpter);
        } else {
            songListAdpter.notifyDataSetChanged();
        }
    }

    private void findId() {
        adapter_lay = findViewById(R.id.adapter_lay);
        back = findViewById(R.id.back);
        no_item_lay = findViewById(R.id.no_item_lay);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Music");
        mTvTitle = findViewById(R.id.title);
        appBarLayout = findViewById(R.id.appbar);
        mTvAlbumAuthorName = findViewById(R.id.author_name);
        mIvAlbumAuthorImage = findViewById(R.id.author_image);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        //playVideoBackground();

    }


}