package com.musicapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bcgdv.asia.lib.ticktock.TickTockView;
import com.musicapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class FadeAwayFragment extends Fragment implements View.OnClickListener {
    Context context;
    TickTockView mCountUp;
    private TextView pause, cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fade_away, container, false);
        context = container.getContext();
        initView(view);

        if (getArguments() != null) {
            String hours = getArguments().getString("hours");
            String min = getArguments().getString("min");
            String sec = getArguments().getString("second");

            Log.e("TIME_COMING", "Hours" + "" + hours + "<<<minutes" + min + "<<<<seconds" + sec);
        }



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

        return view;
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

    private void initView(View view) {
        mCountUp = (TickTockView) view.findViewById(R.id.mCountUp);
        pause = view.findViewById(R.id.pause);
        cancel = view.findViewById(R.id.cancel);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);

     /*   CircularProgressIndicator circularProgress = view.findViewById(R.id.circular_progress);

// you can set max and current progress values individually
        circularProgress.setMaxProgress(10000);
        circularProgress.setCurrentProgress(5000);
// or all at once
        circularProgress.setProgress(5000, 10000);

// you can get progress values using following getters
        circularProgress.getProgress(); // returns 5000
        circularProgress.getMaxProgress(); // returns 10000


        circularProgress.setProgressTextAdapter(TIME_TEXT_ADAPTER);*/

    }

    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = time -> {
        int hours = (int) (time / 3600);
        time %= 3600;
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        StringBuilder sb = new StringBuilder();
        if (hours < 10) {
            sb.append(0);
        }
        sb.append(hours).append(":");
        if (minutes < 10) {
            sb.append(0);
        }
        sb.append(minutes).append(":");
        if (seconds < 10) {
            sb.append(0);
        }
        sb.append(seconds);
        return sb.toString();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                mCountUp.stop();
                break;

            case R.id.cancel:

                loadFragment(new EndTimeFragment());
                break;
        }
    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
