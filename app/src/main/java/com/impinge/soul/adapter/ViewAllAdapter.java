package com.impinge.soul.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.impinge.soul.R;
import com.impinge.soul.activities.AudioPlayerViewActivity;
import com.impinge.soul.models.songsPojo;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.PlayerUtil;

import java.util.ArrayList;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {
    Context context;
    ArrayList<songsPojo> songs_list = new ArrayList<>();

    public ViewAllAdapter(Context context, ArrayList<songsPojo> list) {
        this.context = context;
        this.songs_list = list;

        Log.e("F", String.valueOf(songs_list.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.explore_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.song_title.setText(songs_list.get(position).getTitle());
        holder.song_duration.setText(songs_list.get(position).getSongs_length() + " " + "min");
        Glide.with(context).load(songs_list.get(position).getCover_images()).into(holder.song_image);
        holder.song_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerUtil.clearInstance();
                PlayerUtil playerUtil = PlayerUtil.getInstance(context, songs_list.get(position).getAudios());
                playerUtil.stopPlayer();
                playerUtil.setSongsPojo(songs_list.get(position));
                Intent intent = new Intent(context, AudioPlayerViewActivity.class);
                intent.putExtra(Constants.SONG_ID, songs_list.get(position).getId());
                intent.putExtra(Constants.SONG_NAME, songs_list.get(position).getTitle());
                intent.putExtra(Constants.SONG_DESCRIPTION, songs_list.get(position).getDescription());
                intent.putExtra(Constants.SONG_COVER_IMAGE, songs_list.get(position).getCover_images());
                intent.putExtra(Constants.SONG_AUDIO_URL, songs_list.get(position).getAudios());
                intent.putExtra(Constants.SONG_AUTHOR_NAME, songs_list.get(position).getAuthor_name());
                intent.putExtra(Constants.SONG_AUTHOR_IMAGE, songs_list.get(position).getAuthor_image());

                ((Activity) context).startActivityForResult(intent, 909);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView song_image;
        private TextView song_title, song_duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song_image = itemView.findViewById(R.id.song_image);
            song_title = itemView.findViewById(R.id.song_title);
            song_duration = itemView.findViewById(R.id.song_duration);
        }
    }
}
