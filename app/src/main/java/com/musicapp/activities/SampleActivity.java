package com.musicapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.musicapp.R;
import com.musicapp.audio_mixer.AudioMixer;
import com.musicapp.audio_mixer.input.AudioInput;
import com.musicapp.audio_mixer.input.BlankAudioInput;
import com.musicapp.audio_mixer.input.GeneralAudioInput;
import com.musicapp.dummy.AudioInputSettingsDialog;
import com.musicapp.dummy.Input;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;

public class SampleActivity extends AppCompatActivity {
    Context context = this;
    private RelativeLayout create_mix, nature_effect_lay;
    NiceSpinner niceSpinner;

    private String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "audio_mixer_output.mp3";
    private List<Input> inputs = new ArrayList<>();
    private AudioMixer audioMixer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
       // findId();
         niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = (String) parent.getItemAtPosition(position);

                Log.e("POSITIONNNNNN",item);

            }
        });


    }


//    private void findId() {
//        nature_effect_lay = findViewById(R.id.nature_effect_lay);
//        create_mix = findViewById(R.id.create_mix);
//        nature_effect_lay = findViewById(R.id.nature_effect_lay);
//
//
//        create_mix.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (inputs.size() > 1) {
//                    startMixing();
//                } else {
//                    Toast.makeText(context, "please select 2 music items", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//        nature_effect_lay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openChooser();
//            }
//        });
//    }
//
//
//    public void openChooser() {
//        Intent intent = new Intent();
//        intent.setType("audio/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(context, data.getData());
//                String dur = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                long duration = Integer.parseInt(dur) * 1000; // milli to micro second
//                retriever.release();
//
//                Input input = new Input();
//                input.uri = data.getData();
//                input.durationUs = duration;
//                inputs.add(input);
//
//
//                Toast.makeText(context, inputs.size() + " input(s) added.", Toast.LENGTH_SHORT).show();
//
//                AudioInputSettingsDialog dialog = new AudioInputSettingsDialog(context, input);
//                dialog.setCancelable(false);
//                dialog.show();
//
//            } catch (Exception o) {
//                Toast.makeText(context, "Input not added.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    private void startMixing() {
//        //For showing progress
//        ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Mixing audio...");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setCancelable(false);
//        progressDialog.setIndeterminate(false);
//        progressDialog.setProgress(0);
//
//        try {
//            audioMixer = new AudioMixer(outputPath);
//
//            for (Input input : inputs) {
//                AudioInput audioInput;
//                if (input.uri != null) {
//                    GeneralAudioInput ai = new GeneralAudioInput(context, input.uri, null);
//                    ai.setStartOffsetUs(input.startOffsetUs);
//                    ai.setStartTimeUs(input.startTimeUs); // optional
//                    ai.setEndTimeUs(input.endTimeUs); // optional
//                    //ai.setVolume(0.5f); //optional
//
//                    audioInput = ai;
//                } else {
//                    audioInput = new BlankAudioInput(5000000);
//                }
//                audioMixer.addDataSource(audioInput);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //audioMixer.setSampleRate(44100);  // optional
//        //audioMixer.setBitRate(128000); // optional
//        //audioMixer.setChannelCount(2); // 1 or 2 // optional
//        //audioMixer.setLoopingEnabled(true); // Only works for parallel mixing
//        audioMixer.setMixingType(AudioMixer.MixingType.PARALLEL);
//        audioMixer.setProcessingListener(new AudioMixer.ProcessingListener() {
//            @Override
//            public void onProgress(double progress) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.setProgress((int) (progress * 100));
//                    }
//                });
//            }
//
//            @Override
//            public void onEnd() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.setProgress(100);
//                        progressDialog.dismiss();
//                        Toast.makeText(context, "Success!!! Ouput path: " + outputPath, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });
//
//        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "End", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                audioMixer.stop();
//                audioMixer.release();
//            }
//        });
//
//        try {
//            audioMixer.start();
//            audioMixer.processAsync();
//            progressDialog.show();
//        } catch (IOException e) {
//            audioMixer.release();
//        }
//    }


}