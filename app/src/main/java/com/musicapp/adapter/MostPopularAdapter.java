package com.musicapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musicapp.R;
import com.musicapp.activities.AlbumActivity;
import com.musicapp.activities.FavouriteListActivity;
import com.musicapp.models.MeditationPojo;
import com.musicapp.models.MostPopularPojo;
import com.musicapp.models.TodaySpecialPojo;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.util.Constants;

import java.util.ArrayList;

public class MostPopularAdapter extends RecyclerView.Adapter<MostPopularAdapter.ViewHolder> {
    Context context;
    ArrayList<songsPojo> mostPopularPojoArrayList;

    public MostPopularAdapter(Context context) {
        this.context = context;
    }

//    public MostPopularAdapter(Activity activity, ArrayList<MostPopularPojo> mostPopularPojoArrayList) {
//        this.context = activity;
//        this.mostPopularPojoArrayList = mostPopularPojoArrayList;
//    }

    public MostPopularAdapter(FragmentActivity activity, ArrayList<songsPojo> most_popular_list) {
        this.context=activity;
        this.mostPopularPojoArrayList=most_popular_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.most_popular, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTvSongTitle.setText(mostPopularPojoArrayList.get(position).getTitle());
        holder.mTvSongDuration.setText(mostPopularPojoArrayList.get(position).getSongs_length() + " " + "min");
        Glide.with(context).load(mostPopularPojoArrayList.get(position).getCover_images()).into(holder.mTvSongCover);

        holder.mRvMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(Constants.ALBUM_ID, mostPopularPojoArrayList.get(position).getId());
                intent.putExtra(Constants.ALBUM_TITILE, mostPopularPojoArrayList.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_DESCRIPTION, mostPopularPojoArrayList.get(position).getTitle());
                intent.putExtra(Constants.ALBUM_AUTHOR_NAME, mostPopularPojoArrayList.get(position).getAuthor_name());
                intent.putExtra(Constants.ALBUM_AUTHOR_IMAGE, mostPopularPojoArrayList.get(position).getAuthor_image());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mostPopularPojoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mTvSongCover;
        TextView mTvSongTitle;
        TextView mTvSongDuration;

        RelativeLayout mRvMainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvSongCover = (ImageView) itemView.findViewById(R.id.song_cover_pic);
            mTvSongDuration = (TextView) itemView.findViewById(R.id.song_duration);
            mTvSongTitle = (TextView) itemView.findViewById(R.id.song_title);
            mRvMainLayout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
        }
    }
}
