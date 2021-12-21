package com.impinge.soul.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.impinge.soul.R;
import com.impinge.soul.adapter.AlarmSoundAdapter;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    Context context=this;
    RecyclerView alarmsound_recyclerView;
    AlarmSoundAdapter alarmSoundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_sound);
        findId();
        setAdapter();

    }
    private void setAdapter() {
        alarmSoundAdapter = new AlarmSoundAdapter(context);
        alarmsound_recyclerView.setAdapter(alarmSoundAdapter);
    }

    private void findId() {
        title = findViewById(R.id.title);
        title.setText("Alarm Sound");
        back = findViewById(R.id.back);
        alarmsound_recyclerView = findViewById(R.id.alarmsound_recyclerView);
        alarmsound_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        alarmsound_recyclerView.setHasFixedSize(true);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}