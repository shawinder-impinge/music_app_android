package com.impinge.soul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.impinge.soul.R;
import com.impinge.soul.models.NotificationData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<NotificationData> list = new ArrayList<>();

    public NotificationAdapter(Context context, ArrayList<NotificationData> notificationList) {
        this.context = context;
        this.list = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.notification_image);
        holder.notification_text.setText(list.get(position).getMessage());

        String time = "";
//        time = Constants.getTimeAgo(Long.parseLong(list.get(position).getCreated()), context);
//        holder.notification_time.setText(time);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView notification_image;
        TextView notification_text, notification_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_image = itemView.findViewById(R.id.notification_image);
            notification_text = itemView.findViewById(R.id.notification_text);
            notification_time = itemView.findViewById(R.id.notification_time);
        }
    }
}
