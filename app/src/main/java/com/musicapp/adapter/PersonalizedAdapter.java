package com.musicapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musicapp.R;
import com.musicapp.activities.PersonalizeActivity;
import com.musicapp.models.PersonalizedModel;

import java.util.ArrayList;

public class PersonalizedAdapter extends RecyclerView.Adapter<PersonalizedAdapter.ViewHolder> {
    Context context;
    ArrayList<PersonalizedModel> personalizedModels = new ArrayList<>();


    public ArrayList<String> selectedList= new ArrayList<>();
    public int selected_position=0;

    public PersonalizedAdapter(Context context) {
        this.context=context;
    }

    public PersonalizedAdapter(Context context, ArrayList<PersonalizedModel> personalizedModelArrayList) {

        this.context=context;
        this.personalizedModels = personalizedModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.personalized_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.mPersonalizedText.setText(personalizedModels.get(position).getPersonalizedValue());
        if(!personalizedModels.get(position).isSelected()){

            holder.mPersonalizedText.setTextColor(Color.parseColor("#000000"));
            holder.mPersonalizedText.setBackgroundResource(R.drawable.cornor_bg_gray);
        }else {
            holder.mPersonalizedText.setBackgroundResource(R.drawable.cornor_bg);
            holder.mPersonalizedText.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        holder.mPersonalizedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selected_position = position;
                if(personalizedModels.get(position).isSelected()){

                    selectedList.remove(personalizedModels.get(position).getPersonalizedValue());
                    personalizedModels.get(position).setSelected(false);
                    holder.mPersonalizedText.setTextColor(Color.parseColor("#000000"));
                    holder.mPersonalizedText.setBackgroundResource(R.drawable.cornor_bg_gray);
                }else {
                    selectedList.add(personalizedModels.get(position).getPersonalizedValue());


                    personalizedModels.get(position).setSelected(true);

                    holder.mPersonalizedText.setBackgroundResource(R.drawable.cornor_bg);
                    holder.mPersonalizedText.setTextColor(Color.parseColor("#FFFFFFFF"));
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return personalizedModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mPersonalizedText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPersonalizedText = (TextView)itemView.findViewById(R.id.personalize_text);

        }
    }
}