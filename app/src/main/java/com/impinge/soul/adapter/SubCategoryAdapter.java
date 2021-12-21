package com.impinge.soul.adapter;

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
import com.impinge.soul.R;
import com.impinge.soul.activities.AlbumActivity;
import com.impinge.soul.models.CategorySongs;
import com.impinge.soul.util.Constants;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<CategorySongs> sub_category_list = new ArrayList<>();


    public SubCategoryAdapter(Context context, ArrayList<CategorySongs> category_list) {
        this.sub_category_list = category_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_category_item, parent, false);
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
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(Constants.ALBUM_ID, sub_category_list.get(position).getId());
                intent.putExtra(Constants.ALBUM_TITILE, sub_category_list.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_DESCRIPTION, sub_category_list.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_COVER_IMAGE, sub_category_list.get(position).getCover_images());
                intent.putExtra(Constants.ALBUM_AUTHOR_NAME, sub_category_list.get(position).getAuthor_name());
                intent.putExtra(Constants.ALBUM_AUTHOR_IMAGE, sub_category_list.get(position).getAuthor_image());
                context.startActivity(intent);
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
}
