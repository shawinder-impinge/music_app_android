package com.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.musicapp.R;
import com.musicapp.adapter.MusicLengthAdapter;

public class MusicLengthActivity extends AppCompatActivity {
    TabLayout tablayout;
    ViewPager viewpager;
    private TextView title;
    MusicLengthAdapter musicLengthAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_length);
        findId();
        setTabLayout();
    }

    private void findId() {
        tablayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.viewpager);
        title = findViewById(R.id.title);
        title.setText("Music Length");

    }

    private void setTabLayout() {
        tablayout.addTab(tablayout.newTab().setText("Fade Away"));
        tablayout.addTab(tablayout.newTab().setText("Alarm Sound"));
        musicLengthAdapter = new MusicLengthAdapter(getSupportFragmentManager(), tablayout.getTabCount());
        viewpager.setAdapter(musicLengthAdapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewpager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}