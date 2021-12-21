package com.impinge.soul.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.impinge.soul.fragments.SetDurationFragment;
import com.impinge.soul.fragments.EndTimeFragment;

public class MusicLengthAdapter extends FragmentStatePagerAdapter {
    int tab_count;

    public MusicLengthAdapter(FragmentManager fm, int tabcount) {
        super(fm);
        this.tab_count = tabcount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EndTimeFragment fadeAwayFragment = new EndTimeFragment();
                return fadeAwayFragment;

            case 1:
                SetDurationFragment alarmSoundFragment = new SetDurationFragment();
                return alarmSoundFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return tab_count;
    }
}
