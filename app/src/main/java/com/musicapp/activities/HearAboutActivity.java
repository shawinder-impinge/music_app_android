package com.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.musicapp.R;

public class HearAboutActivity extends BaseActivity implements View.OnClickListener {
    private ImageView cross;
    Context context = this;
    private RadioButton radio_awaking, radio_friends, radio_woke, radio_tharapist, radio_moonlight, radio_power, radio_app_store, radio_app_zen, radio_online,
            radio_nature, radio_astral, radio_self, radio_personal, radio_other;
    private RadioGroup radiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hear_about);
        fullWindow();
        findId();
    }



    private void findId() {
        cross = findViewById(R.id.cross);
        radiogroup = findViewById(R.id.radiogroup);
        radio_awaking = findViewById(R.id.radio_awaking);
        radio_friends = findViewById(R.id.radio_friends);
        radio_woke = findViewById(R.id.radio_woke);
        radio_tharapist = findViewById(R.id.radio_tharapist);
        radio_moonlight = findViewById(R.id.radio_moonlight);
        radio_power = findViewById(R.id.radio_power);
        radio_app_store = findViewById(R.id.radio_app_store);
        radio_app_zen = findViewById(R.id.radio_app_zen);
        radio_online = findViewById(R.id.radio_online);
        radio_nature = findViewById(R.id.radio_nature);
        radio_astral = findViewById(R.id.radio_astral);
        radio_self = findViewById(R.id.radio_self);
        radio_personal = findViewById(R.id.radio_personal);
        radio_other = findViewById(R.id.radio_other);
        cross.setOnClickListener(this);
        checkRadioButton();
    }

    private void checkRadioButton() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.findViewById(checkedId).getId()){
                    case R.id.radio_awaking:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_friends:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_woke:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_tharapist:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_moonlight:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_power:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_app_store:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_app_zen:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_online:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_nature:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_astral:
                        redirectToPersonalize();
                        break;

                    case R.id.radio_self:
                        redirectToPersonalize();
                        break;

                    case R.id.radio_personal:
                        redirectToPersonalize();
                        break;
                    case R.id.radio_other:
                        redirectToPersonalize();
                        break;


                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cross:
                finish();
                break;
        }
    }

    private void redirectToPersonalize() {
        startActivity(new Intent(HearAboutActivity.this, PersonalizeActivity.class));
        overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

    }
}