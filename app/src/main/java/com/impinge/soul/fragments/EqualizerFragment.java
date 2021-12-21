package com.impinge.soul.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.impinge.soul.R;
import com.impinge.soul.activities.BinaturalBeatActivity;
import com.impinge.soul.activities.EffectsActivity;
import com.impinge.soul.activities.MusicLength;
import com.impinge.soul.activities.VolumeMixerActivity;
import com.impinge.soul.audio_mixer.AudioMixer;
import com.impinge.soul.dummy.Input;

import java.util.ArrayList;
import java.util.List;

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
                //mixAudioSongs();
                break;
        }
    }

//    private void mixAudioSongs() {
//        AudioInput input1 = new GeneralAudioInput(input1Path);
//        input1.setVolume(0.5f); //Optional
//        // It will produce a blank portion of 3 seconds between input1 and input2 if mixing type is sequential.
//        // But it will does nothing in parallel mixing.
//        AudioInput blankInput = new BlankAudioInput(3000000); //
//        AudioInput input2 = new GeneralAudioInput(this, input2Uri, null);
//        input2.setStartTimeUs(3000000); //Optional
//        input2.setEndTimeUs(9000000); //Optional
//       // input2.setStartOffsetUs(5000000); //Optional. It is needed to start mixing the input at a certain time.
//        String outputPath = Environment.getDownloadCacheDirectory().getAbsolutePath()
//                +"/" +"audio_mixer_output.mp3"; // for example
//        final AudioMixer audioMixer;
//        try {
//            audioMixer = new AudioMixer(outputPath);
//            audioMixer.addDataSource(input1);
//            audioMixer.addDataSource(blankInput);
//            audioMixer.addDataSource(input2);
//            audioMixer.setSampleRate(44100); // Optional
//            audioMixer.setBitRate(128000); // Optional
//            audioMixer.setChannelCount(2); // Optional //1(mono) or 2(stereo)
//
//            // Smaller audio inputs will be encoded from start-time again if it reaches end-time
//            // It is only valid for parallel mixing
//            //audioMixer.setLoopingEnabled(true);
//            audioMixer.setMixingType(AudioMixer.MixingType.PARALLEL); // or AudioMixer.MixingType.SEQUENTIAL
//            audioMixer.setProcessingListener(new AudioMixer.ProcessingListener() {
//                @Override
//                public void onProgress(final double progress) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressDialog.setProgress((int) (progress * 100));
//                        }
//                    });
//                }
//                @Override
//                public void onEnd() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(context, "Success!!!", Toast.LENGTH_SHORT).show();
//                            audioMixer.release();
//                        }
//                    });
//                }
//            });
//            //it is for setting up the all the things
//            audioMixer.start();
//
//            /* These getter methods must be called after calling 'start()'*/
//            //audioMixer.getOutputSampleRate();
//            //audioMixer.getOutputBitRate();
//            //audioMixer.getOutputChannelCount();
//            //audioMixer.getOutputDurationUs();
//
//            //starting real processing
//            audioMixer.processAsync();
//
//            // We can stop the processing immediately by calling audioMixer.stop() when we want.
//
//            // audioMixer.processSync() is generally not used.
//            // We have to use this carefully.
//            // Tt will do the processing in caller thread
//            // And calling audioMixer.stop() from the same thread won't stop the processing
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    // TODO: 28-09-2021 audio mixer gitub link
    /*https://github.com/ZeroOneZeroR/android_audio_mixer*/

    @Override
    public void onResume() {
        super.onResume();
        Log.e("log_value", "onResume");
    }
}
