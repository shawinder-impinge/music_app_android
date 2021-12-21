package com.impinge.soul.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.impinge.soul.R;
import com.impinge.soul.models.CategoryModel;
import com.impinge.soul.models.CategorySongs;

import java.util.ArrayList;

public class MusicCategoryAdapter extends RecyclerView.Adapter<MusicCategoryAdapter.ViewHolder> {
    Context context;
    OnClick onClick;

    ArrayList<CategoryModel> category_list = new ArrayList<>();

    public MusicCategoryAdapter(Context context, ArrayList<CategoryModel> list) {
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

        holder.category_name.setText(category_list.get(position).getTitle());
//        category_song_list.clear();
//        category_song_list.addAll(category_list.get(position).getSongs());
        setAdapter(category_list.get(position).getSongs(), holder.subCategoryAdapter, holder.sub_category_recycler);

        holder.viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item_name", category_list.get(position).getTitle());
                onClick.onItemclick(String.valueOf(category_list.get(position).getId()), String.valueOf(holder.getAdapterPosition()),category_list.get(position).getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name, viewall;
        SubCategoryAdapter subCategoryAdapter;
        RecyclerView sub_category_recycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            viewall = itemView.findViewById(R.id.viewall);
            sub_category_recycler = itemView.findViewById(R.id.sub_category_recycler);
            sub_category_recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            sub_category_recycler.setHasFixedSize(true);


        }
    }

    private void setAdapter(ArrayList<CategorySongs> category_list, SubCategoryAdapter subCategoryAdapter, RecyclerView sub_category_recycler) {
        subCategoryAdapter = new SubCategoryAdapter(context, category_list);
        sub_category_recycler.setAdapter(subCategoryAdapter);

    }


    public void viewItemClick(OnClick onClick) {
        this.onClick = onClick;

    }

    public interface OnClick {
        public void onItemclick(String id, String pos,String title);
    }
}
