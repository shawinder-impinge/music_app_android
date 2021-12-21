package com.impinge.soul.activities;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcgdv.asia.lib.ticktock.TickTockView;
import com.impinge.soul.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StartDurationActivity extends PlayerBaseActivity implements View.OnClickListener {
    Context context=this;
    TextView title;
    TickTockView mCountUp;
    private TextView pause, cancel;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_duration);
        findId();
        if (mCountUp != null) {
            mCountUp.setOnTickListener(new TickTockView.OnTickListener() {
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                Date date = new Date();

                @Override
                public String getText(long timeRemaining) {
                    date.setTime(System.currentTimeMillis());
                    return format.format(date);
                }
            });
        }
    }

    private void findId() {
        mCountUp = (TickTockView) findViewById(R.id.mCountUp);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        title.setText("Alarm");
        pause = findViewById(R.id.pause);
        cancel = findViewById(R.id.cancel);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MINUTE, 4);
        end.add(Calendar.SECOND, 5);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MINUTE, -1);

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.HOUR, 2);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        if (mCountUp != null) {
            mCountUp.start(c2);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mCountUp.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                mCountUp.stop();
                break;

            case R.id.cancel:

               // loadFragment(new EndTimeFragment());
                finish();
                break;
            case R.id.back:
                finish();
                break;


        }
    }
    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}