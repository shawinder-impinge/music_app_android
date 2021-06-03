package com.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicapp.R;

public class VolumeMixerActivity extends AppCompatActivity {
    private TextView title;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_mixer);
        findId();
    }

    private void findId() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        title.setText("Volume Mixer");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}