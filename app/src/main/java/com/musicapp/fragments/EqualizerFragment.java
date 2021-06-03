package com.musicapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.musicapp.R;
import com.musicapp.activities.BinaturalBeatActivity;
import com.musicapp.activities.EffectsActivity;
import com.musicapp.activities.MusicLength;
import com.musicapp.activities.VolumeMixerActivity;
import com.musicapp.audio_mixer.AudioMixer;
import com.musicapp.audio_mixer.input.AudioInput;
import com.musicapp.audio_mixer.input.BlankAudioInput;
import com.musicapp.audio_mixer.input.GeneralAudioInput;
import com.musicapp.dummy.Input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;

public class EqualizerFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout nature_effect_lay, binaural_beat_lay, volume_mixer_lay, music_length_lay;
    private TextView title;
    private ImageView back;
    private List<Input> inputs = new ArrayList<>();
    AudioMixer audioMixer;
    private String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "audio_mixer_output.mp3";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.equalizer, container, false);
        findId(view);
        return view;
    }

    private void findId(View view) {
        title = view.findViewById(R.id.title);
        back = view.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        nature_effect_lay = view.findViewById(R.id.nature_effect_lay);
        binaural_beat_lay = view.findViewById(R.id.binaural_beat_lay);
        volume_mixer_lay = view.findViewById(R.id.volume_mixer_lay);
        music_length_lay = view.findViewById(R.id.music_length_lay);
        nature_effect_lay.setOnClickListener(this);
        binaural_beat_lay.setOnClickListener(this);
        volume_mixer_lay.setOnClickListener(this);
        music_length_lay.setOnClickListener(this);
        title.setText("Sound Effects");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nature_effect_lay:

                startActivity(new Intent(getActivity(), EffectsActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

                break;
            case R.id.binaural_beat_lay:
                startActivity(new Intent(getActivity(), BinaturalBeatActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;
            case R.id.volume_mixer_lay:
                startActivity(new Intent(getActivity(), VolumeMixerActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;
            case R.id.music_length_lay:
                //startActivity(new Intent(getActivity(), MusicLengthActivity.class));
                startActivity(new Intent(getActivity(), MusicLength.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("log_value", "onResume");
    }
}
