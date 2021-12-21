package com.impinge.soul.activities;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.impinge.soul.R;
import com.impinge.soul.fragments.SetDurationFragment;
import com.impinge.soul.fragments.EndTimeFragment;

public class MusicLength extends PlayerBaseActivity implements View.OnClickListener {
    private TextView end_time, duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_length2);
        findId();
        loadFragment(new EndTimeFragment());
        end_time.setBackgroundColor(getResources().getColor(R.color.gray_light));
        duration.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    private void findId() {
        end_time = findViewById(R.id.end_time);
        duration = findViewById(R.id.duration);
        end_time.setOnClickListener(this);
        duration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_time:

                loadFragment(new EndTimeFragment());
                end_time.setBackgroundColor(getResources().getColor(R.color.gray_light));
                duration.setBackgroundColor(getResources().getColor(R.color.gray));
                break;

            case R.id.duration:

                loadFragment(new SetDurationFragment());
                end_time.setBackgroundColor(getResources().getColor(R.color.gray));
                duration.setBackgroundColor(getResources().getColor(R.color.gray_light));
                break;

        }
    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}