package com.musicapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musicapp.R;
import com.musicapp.models.CategoryModel;
import com.musicapp.models.CategorySongs;
import com.musicapp.models.EffectsAlbumPojo;
import com.musicapp.models.EffectsPojo;
import com.musicapp.models.songsPojo;

import java.util.ArrayList;

public class WeatherCategoryAdapter extends RecyclerView.Adapter<WeatherCategoryAdapter.ViewHolder> {
    Context context;
    EffectClick effectClick;


    ArrayList<EffectsAlbumPojo> category_list = new ArrayList<>();
    ArrayList<CategorySongs> category_song_list = new ArrayList<>();


    public WeatherCategoryAdapter(Context context, ArrayList<EffectsAlbumPojo> list) {
        this.context = context;
        this.category_list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.category_name.setText(category_list.get(position).getName());
//        category_song_list.clear();
//        category_song_list.addAll(category_list.get(position).getSongs());
        setAdapter(category_list.get(position).getEffect_songs(), holder.subCategoryAdapter, holder.sub_category_recycler);

    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name;
        EffectSubCategoryAdapter subCategoryAdapter;
        RecyclerView sub_category_recycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            sub_category_recycler = itemView.findViewById(R.id.sub_category_recycler);
            sub_category_recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            sub_category_recycler.setHasFixedSize(true);


        }
    }

    private void setAdapter(ArrayList<songsPojo> category_list, EffectSubCategoryAdapter subCategoryAdapter, RecyclerView sub_category_recycler) {
        subCategoryAdapter = new EffectSubCategoryAdapter(context, category_list);
        sub_category_recycler.setAdapter(subCategoryAdapter);

        subCategoryAdapter.onImageClick(new EffectSubCategoryAdapter.Click() {
            @Override
            public void onItemClick(String id, String pos,String url,String desc,String title) {
                effectClick.click(id, pos,url,desc,title);
            }
        });
    }


    public void onEffectClick(EffectClick effectClick) {
        this.effectClick = effectClick;

    }

    public interface EffectClick {
        public void click(String id, String pos,String url,String desc,String title);
    }
}
