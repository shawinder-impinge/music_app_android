package com.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.musicapp.R;
import com.musicapp.fragments.ForYouFragment;
import com.musicapp.fragments.EqualizerFragment;
import com.musicapp.fragments.MusicFragment;
import com.musicapp.fragments.SearchFragment;
import com.musicapp.fragments.SettingFragment;
import com.musicapp.util.PlayerUtil;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;

public class DashboardActivity extends PlayerBaseActivity {
    Context context = this;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        loadFragment(new ForYouFragment());
        setNavigationBottomBar();
    }

    private void setNavigationBottomBar() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.for_you:
                        fragment = new ForYouFragment();

                        break;

                    case R.id.music:
                        fragment = new MusicFragment();
                        break;
                    case R.id.equilizer:
                        fragment = new EqualizerFragment();
                        break;
                    case R.id.setting:
                        fragment = new SettingFragment();
                        break;
                    case R.id.search:
                        fragment = new SearchFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PlayerUtil playerUtil = PlayerUtil.getInstance(this, "");
//        if (playerUtil!=null){
//            playerUtil.stopPlayer();
//            PlayerUtil.clearInstance();
//        }

        Log.e("onDestroy", "dashboard");

    }
}