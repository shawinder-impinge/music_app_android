package com.impinge.soul.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.impinge.soul.R;
import com.impinge.soul.activities.AlbumActivity;
import com.impinge.soul.models.CategoryModel;
import com.impinge.soul.util.Constants;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<CategoryModel> category_list = new ArrayList<>();

    public CategoryAdapter(Context context, ArrayList<CategoryModel> search_category_list) {
        this.category_list = search_category_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_items, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (category_list.get(position).getName() != null) {
            holder.song_title.setText(category_list.get(position).getTitle());

        }
        if (category_list.get(position).getCover_images() != null) {
            Glide.with(context).load(category_list.get(position).getCover_images()).into(holder.album_image);
        }
        holder.album_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(Constants.ALBUM_ID, category_list.get(position).getId());
                intent.putExtra(Constants.ALBUM_TITILE, category_list.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_DESCRIPTION, category_list.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_AUTHOR_NAME, category_list.get(position).getAuthor_name());
                intent.putExtra(Constants.ALBUM_AUTHOR_IMAGE, category_list.get(position).getAuthor_image());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView song_title;
        private ImageView album_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song_title = itemView.findViewById(R.id.song_title);
            album_image = itemView.findViewById(R.id.album_image);
        }
    }
}
