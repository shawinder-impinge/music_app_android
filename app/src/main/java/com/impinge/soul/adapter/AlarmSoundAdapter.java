package com.impinge.soul.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.impinge.soul.R;

public class AlarmSoundAdapter extends RecyclerView.Adapter<AlarmSoundAdapter.ViewHolder> {
    Context context;
    public int mSelectedItem = -1;


    public AlarmSoundAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_sound_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.radio_button.setChecked(position == mSelectedItem);


        holder.radio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = holder.getAdapterPosition();
                notifyDataSetChanged();
                Log.e("mSelectedItem", String.valueOf(mSelectedItem));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radio_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radio_button = itemView.findViewById(R.id.radio_button);
        }
    }
}
