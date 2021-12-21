package com.impinge.soul.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.impinge.soul.R;
import com.impinge.soul.fragments.ForYouFragment;
import com.impinge.soul.fragments.EqualizerFragment;
import com.impinge.soul.fragments.MusicFragment;
import com.impinge.soul.fragments.SearchFragment;
import com.impinge.soul.fragments.SettingFragment;

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