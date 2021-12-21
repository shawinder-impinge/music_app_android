package com.impinge.soul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.impinge.soul.R;
import com.impinge.soul.localdatabase.DownloadedSongsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DownloadSongsAdapter extends RecyclerView.Adapter<DownloadSongsAdapter.ViewHolder> {
    Context context;
    List<DownloadedSongsModel> downloaded_songs_list;

    public DownloadSongsAdapter(Context context, List<DownloadedSongsModel> tasks) {
        this.context = context;
        this.downloaded_songs_list = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.download_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.song_name.setText(downloaded_songs_list.get(position).getName());
        holder.song_duration.setText(downloaded_songs_list.get(position).getDuration() + " " + "min");
        Glide.with(context).load(downloaded_songs_list.get(position).getImage()).into(holder.song_image);
        holder.main_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* PlayerUtil.clearInstance();
                PlayerUtil playerUtil = PlayerUtil.getInstance(context, downloaded_songs_list.get(position).getSong_url());
                playerUtil.stopPlayer();
                playerUtil.setDownloadedSongsModel(downloaded_songs_list.get(position));
                Intent intent = new Intent(context, AudioPlayerViewActivity.class);
                intent.putExtra(Constants.SONG_ID, downloaded_songs_list.get(position).getId());
                intent.putExtra(Constants.SONG_NAME, downloaded_songs_list.get(position).getName());
                intent.putExtra(Constants.SONG_DESCRIPTION, downloaded_songs_list.get(position).getSong_desc());
                intent.putExtra(Constants.SONG_COVER_IMAGE, downloaded_songs_list.get(position).getImage());
                intent.putExtra(Constants.SONG_AUDIO_URL, downloaded_songs_list.get(position).getSong_url());
                intent.putExtra(Constants.SONG_AUTHOR_NAME, downloaded_songs_list.get(position).getAuthor_name());
                intent.putExtra(Constants.SONG_AUTHOR_IMAGE, downloaded_songs_list.get(position).getAuthor_image());

                ((Activity) context).startActivityForResult(intent, 909);
*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return downloaded_songs_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView song_image;
        private TextView song_name, song_duration;
        private RelativeLayout main_lay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song_name = itemView.findViewById(R.id.song_name);
            song_image = itemView.findViewById(R.id.song_image);
            song_duration = itemView.findViewById(R.id.song_duration);
            main_lay = itemView.findViewById(R.id.main_lay);
        }
    }
}
