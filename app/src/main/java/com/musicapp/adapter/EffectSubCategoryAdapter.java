package com.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musicapp.R;
import com.musicapp.activities.AlbumActivity;
import com.musicapp.models.CategorySongs;
import com.musicapp.models.EffectsAlbumPojo;
import com.musicapp.models.songsPojo;
import com.musicapp.util.Constants;

import java.util.ArrayList;

public class EffectSubCategoryAdapter extends RecyclerView.Adapter<EffectSubCategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<songsPojo> sub_category_list = new ArrayList<>();
    Click click;


    public EffectSubCategoryAdapter(Context context, ArrayList<songsPojo> category_list) {
        this.sub_category_list = category_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_category_item_nature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Glide.with(context).load(sub_category_list.get(position).getCover_images()).into(holder.song_cover_pic);
        holder.song_title.setText(sub_category_list.get(position).getTitle());
        holder.song_duration.setText(sub_category_list.get(position).getSongs_length() + " " + "minutes");

        Glide.with(context).load(sub_category_list.get(position).getCover_images()).into(holder.song_cover_pic);

     /*   if (!sub_category_list.get(position).getCover_images().isEmpty() && sub_category_list.get(position).getCover_images() != null) {

        } else {
            Glide.with(context).load(R.drawable.user_placeholder).into(holder.song_cover_pic);
        }*/
        holder.relative_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.onItemClick(String.valueOf(sub_category_list.get(position).getId()), String.valueOf(holder.getAdapterPosition()),
                        sub_category_list.get(position).getAudios(),sub_category_list.get(position).getDescription(),sub_category_list.get(position).getTitle());
                /*Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(Constants.ALBUM_ID, sub_category_list.get(position).getId());
                intent.putExtra(Constants.ALBUM_TITILE, sub_category_list.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_DESCRIPTION, sub_category_list.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_COVER_IMAGE, sub_category_list.get(position).getCover_images());
                intent.putExtra(Constants.ALBUM_AUTHOR_NAME, sub_category_list.get(position).getAuthor_name());
                intent.putExtra(Constants.ALBUM_AUTHOR_IMAGE, sub_category_list.get(position).getAuthor_image());
                context.startActivity(intent);*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return sub_category_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView song_title, song_duration;
        private ImageView song_cover_pic;
        private RelativeLayout relative_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song_title = itemView.findViewById(R.id.song_title);
            song_duration = itemView.findViewById(R.id.song_duration);
            song_cover_pic = itemView.findViewById(R.id.song_cover_pic);
            relative_main = itemView.findViewById(R.id.relative_main);
        }
    }

    public void onImageClick(Click click) {
        this.click = click;
    }

    public interface Click {
        public void onItemClick(String id, String pos,String url,String desc,String title);
    }
}
