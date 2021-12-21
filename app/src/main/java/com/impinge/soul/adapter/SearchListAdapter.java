package com.impinge.soul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.impinge.soul.R;
import com.impinge.soul.models.SearchSongModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    Context context;
    ArrayList<SearchSongModel> songList = new ArrayList<>();

    public SearchListAdapter(FragmentActivity activity, ArrayList<SearchSongModel> searchList) {
        this.context = activity;
        this.songList = searchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.song_name.setText(songList.get(position).getName());
        holder.song_length.setText(songList.get(position).getSongs_length() + " " + "minutes");

        if (!songList.get(position).getCover_images().isEmpty() && songList.get(position).getCover_images() != null) {
            Glide.with(context).load(songList.get(position).getCover_images()).into(holder.song_image);

        } else {
            Glide.with(context).load(R.drawable.dummy_profile).into(holder.song_image);

        }

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView song_image;
        TextView song_name, song_length;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song_image = itemView.findViewById(R.id.song_image);
            song_name = itemView.findViewById(R.id.song_name);
            song_length = itemView.findViewById(R.id.song_length);
        }
    }
}
