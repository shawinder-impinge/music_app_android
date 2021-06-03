package com.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musicapp.R;
import com.musicapp.activities.AudioPlayerViewActivity;
import com.musicapp.models.ExploreVideos;
import com.musicapp.models.User;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Constants;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    Context context;
    ArrayList<ExploreVideos> video_list = new ArrayList<>();
    MediaPlayer mediaPlayer;
    boolean isFavourite = false;
    String video_id = "";
    String videoUrl = "";
    ExploreVideos videos;


    public VideoAdapter(Context context, ArrayList<ExploreVideos> explore_video_list) {
        this.context = context;
        this.video_list = explore_video_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        videos = video_list.get(position);
        // Glide.with(context).load(R.drawable.ic_launcher_background).into(holder.video_preview);

        if (videos.isIs_user_fav()) {
            holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.music_like));

        } else {
            holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));

        }


        holder.video_name.setText(videos.getName());
        holder.song_duration.setText(videos.getDuration() + " " + "min");
        isFavourite = Boolean.valueOf(videos.isIs_user_fav());

        try {
            video_id = videos.getId();
            videoUrl = videos.getFile();
            Uri video = Uri.parse(/*APIClient.MEDIA_URL +*/ videoUrl);
            holder.video_view.setVideoURI(video);
            holder.video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    if (mediaPlayer == null) {
                        mediaPlayer = mp;
                    }
                    mp.setLooping(true);
                    holder.video_view.start();
                    //holder.video_preview.setVisibility(View.GONE);
                }
            });
            holder.video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //holder.video_preview.setVisibility(View.VISIBLE);
                }
            });
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer == null) return;
                    if (mediaPlayer.isPlaying()) {

                        holder.play.setImageDrawable(context.getResources().getDrawable(R.drawable.play_round));

                        mediaPlayer.pause();
                        holder.video_view.pause();
                    } else {
                        holder.play.setImageDrawable(context.getResources().getDrawable(R.drawable.pause));

                        mediaPlayer.start();
                        holder.video_view.start();
                    }
                }
            });
            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception e) {
            Log.e("video_exception", e.getMessage());
            e.printStackTrace();
        }


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                String app_url = " https://play.google.com/store/apps/details?id=my.example.javatpoint";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, video_list.get(position).getFile());
                context.startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.isNetworkAvailable(context)) {
                    calFavouriteApi(video_id);
                }
            }
        });


    }

    private void calFavouriteApi(String video_id) {

        Utility utility = Utility.getInstance(context);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", SharedPreference.fetchPrefenceData(context, PreferenceData.USER_ID));
        hashMap.put("songs", video_id);

        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);


        if (!isFavourite) {
            Call<RestResponse<User>> callApi = apiInterface.addToFavourite(hashMap);
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        videos.setIs_user_fav(true);


                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();

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
                        videos.setIs_user_fav(false);


                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return video_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView video_view;
        private TextView video_name, song_duration;
        private ImageView share, like, play, video_preview, pause;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            video_view = itemView.findViewById(R.id.video_view);
            play = itemView.findViewById(R.id.play);
            video_name = itemView.findViewById(R.id.video_name);
            share = itemView.findViewById(R.id.share);
            like = itemView.findViewById(R.id.like);
            video_preview = itemView.findViewById(R.id.video_preview);
            pause = itemView.findViewById(R.id.pause);
            song_duration = itemView.findViewById(R.id.song_duration);
        }
    }
}
