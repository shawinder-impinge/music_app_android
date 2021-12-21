package com.impinge.soul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.impinge.soul.util.PlayerUtil;

public class PhoneStateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            System.out.println("Receiver start");
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context,"Ringing State Number is -",Toast.LENGTH_SHORT).show();
                try {
                    PlayerUtil playerUtil = PlayerUtil.getInstance(context, null);

                    playerUtil.simpleExoPlayer.setPlayWhenReady(false);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                try {
                    PlayerUtil playerUtil = PlayerUtil.getInstance(context, null);


                        playerUtil.simpleExoPlayer.setPlayWhenReady(true);

                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
            }        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
