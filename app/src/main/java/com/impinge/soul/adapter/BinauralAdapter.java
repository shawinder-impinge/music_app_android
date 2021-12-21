package com.impinge.soul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.impinge.soul.R;
import com.impinge.soul.models.BinauralBeatModel;
import com.impinge.soul.models.songsPojo;

import java.util.ArrayList;

public class BinauralAdapter extends RecyclerView.Adapter<BinauralAdapter.ViewHolder> {
    Context context;
    private ArrayList<BinauralBeatModel> binauralBeatArrayList = new ArrayList<BinauralBeatModel>();
    ArrayList<songsPojo> binaural_beat_list = new ArrayList<>();

    BinauralClick binauralClick;


    public BinauralAdapter(Context context, ArrayList<songsPojo> binauralBeatArrayList) {
        this.context = context;
        this.binaural_beat_list = binauralBeatArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.binaural_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.song_title.setText(binaural_beat_list.get(position).getTitle());
        Glide.with(context).load(binaural_beat_list.get(position).getCover_images()).into(holder.album_image);

        holder.album_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binauralClick.onClick(String.valueOf(binaural_beat_list.get(position).getId()),String.valueOf(holder.getAdapterPosition()),binaural_beat_list.get(position).getAudios(),
                        binaural_beat_list.get(position).getDescription(),binaural_beat_list.get(position).getTitle());
                /*PlayerUtil.clearInstance();
                PlayerUtil playerUtil = PlayerUtil.getInstance(context,binaural_beat_list.get(position).getAudios());
                playerUtil.stopPlayer();
                playerUtil.setSongsPojo(binaural_beat_list.get(position));
                Intent intent = new Intent(context, AudioPlayerViewActivity.class);
                intent.putExtra(Constants.SONG_ID,binaural_beat_list.get(position).getId());
                intent.putExtra(Constants.SONG_NAME,binaural_beat_list.get(position).getTitle());
                intent.putExtra(Constants.SONG_DESCRIPTION,binaural_beat_list.get(position).getDescription());
                intent.putExtra(Constants.SONG_COVER_IMAGE,binaural_beat_list.get(position).getCover_images());
                intent.putExtra(Constants.SONG_AUDIO_URL,binaural_beat_list.get(position).getAudios());
                intent.putExtra(Constants.SONG_AUTHOR_NAME,binaural_beat_list.get(position).getAuthor_name());
                intent.putExtra(Constants.SONG_AUTHOR_IMAGE,binaural_beat_list.get(position).getAuthor_image());

                ((Activity)context).startActivityForResult(intent,909);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return binaural_beat_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView song_title;
        ImageView album_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song_title = itemView.findViewById(R.id.song_title);
            album_image = itemView.findViewById(R.id.album_image);
        }
    }


    public void BinarialInterfaceClick(BinauralClick binauralClick){
        this.binauralClick=binauralClick;
    }

    public interface BinauralClick{
        public void onClick(String id, String pos,String url,String desc,String title);
    }
}
