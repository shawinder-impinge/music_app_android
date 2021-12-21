package com.impinge.soul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.impinge.soul.R;

import java.util.ArrayList;

public class FreeTrialAdapter extends RecyclerView.Adapter<FreeTrialAdapter.ViewHolder> {
    Context context;
    ArrayList<String> featureLIst = new ArrayList<>();

    public FreeTrialAdapter(Context context, ArrayList<String> featureList) {
        this.context=context;
        this.featureLIst = featureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trial_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTvTitle.setText(featureLIst.get(position));
    }

    @Override
    public int getItemCount() {
        return featureLIst.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvTitle = itemView.findViewById(R.id.title);
        }
    }
}
