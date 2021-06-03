package com.musicapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musicapp.R;
import com.musicapp.activities.AudioPlayerViewActivity;
import com.musicapp.models.User;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Constants;
import com.musicapp.util.PlayerUtil;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongListAdpter extends RecyclerView.Adapter<SongListAdpter.ViewHolder> {
    Context context;
    ArrayList<songsPojo> songsPojoArrayList;


    public SongListAdpter(Context context) {
        this.context = context;
    }

    public SongListAdpter(Activity albumActivity, ArrayList<songsPojo> songsPojoarrayList) {
        this.context = albumActivity;

        this.songsPojoArrayList = songsPojoarrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTvSongName.setText(songsPojoArrayList.get(position).getTitle());
        holder.mTvSongDuration.setText(songsPojoArrayList.get(position).getSongs_length());

        if (songsPojoArrayList.get(position).isIs_user_fav()) {
            holder.mIvLIke.setImageDrawable(context.getResources().getDrawable(R.drawable.liked_heart));
        } else {
            holder.mIvLIke.setImageDrawable(context.getResources().getDrawable(R.drawable.like_black));

        }
        holder.mIvLIke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calFavouriteApi(position);
            }
        });
        holder.mRvMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PlayerUtil.clearInstance();
                PlayerUtil playerUtil = PlayerUtil.getInstance(context, songsPojoArrayList.get(position).getAudios());
                playerUtil.stopPlayer();
                playerUtil.clearInstance();
                playerUtil = PlayerUtil.getInstance(context, songsPojoArrayList.get(position).getAudios());

                playerUtil.setSongsPojo(songsPojoArrayList.get(position));
                Intent intent = new Intent(context, AudioPlayerViewActivity.class);
                intent.putExtra(Constants.SONG_ID, songsPojoArrayList.get(position).getId());
                intent.putExtra(Constants.SONG_NAME, songsPojoArrayList.get(position).getTitle());
                intent.putExtra(Constants.SONG_DESCRIPTION, songsPojoArrayList.get(position).getDescription());
                intent.putExtra(Constants.SONG_COVER_IMAGE, songsPojoArrayList.get(position).getCover_images());
                intent.putExtra(Constants.SONG_AUDIO_URL, songsPojoArrayList.get(position).getAudios());
                intent.putExtra(Constants.SONG_AUTHOR_NAME, songsPojoArrayList.get(position).getAuthor_name());
                intent.putExtra(Constants.SONG_AUTHOR_IMAGE, songsPojoArrayList.get(position).getAuthor_image());
                intent.putExtra("adapter", "");

                ((Activity) context).startActivityForResult(intent, 909);
            }
        });


    }

    @Override
    public int getItemCount() {
        return songsPojoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvSongName;
        TextView mTvSongDuration;
        ImageView mIvLIke, mIvPlay;
        LinearLayout mRvMainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvSongName = (TextView) itemView.findViewById(R.id.song_name);
            mTvSongDuration = (TextView) itemView.findViewById(R.id.duration);
            mIvLIke = (ImageView) itemView.findViewById(R.id.like);
            mIvPlay = (ImageView) itemView.findViewById(R.id.play_layout);
            mRvMainLayout = itemView.findViewById(R.id.main_layout);
        }
    }

    private void calFavouriteApi(int index) {


        Utility utility = Utility.getInstance(context);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", SharedPreference.fetchPrefenceData(context, PreferenceData.USER_ID));
        hashMap.put("songs", songsPojoArrayList.get(index).getId());

        Log.e("SIGNUP_DATA", String.valueOf(hashMap));
        utility.showLoading(context.getResources().getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);


        if (!songsPojoArrayList.get(index).isIs_user_fav()) {
            Call<RestResponse<User>> callApi = apiInterface.addToFavourite(hashMap);
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        songsPojoArrayList.get(index).setIs_user_fav(true);
                        notifyItemChanged(index);

                        utility.hideLoading();

                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } else {


            Call<RestResponse<User>> removecallApi = apiInterface.removeFromFavourite(hashMap);
            removecallApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        songsPojoArrayList.get(index).setIs_user_fav(false);
                        notifyItemChanged(index);
                        utility.hideLoading();

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
}
