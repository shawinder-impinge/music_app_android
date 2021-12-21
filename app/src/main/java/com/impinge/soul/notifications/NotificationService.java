package com.impinge.soul.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.impinge.soul.R;
import com.impinge.soul.activities.AudioPlayerViewActivity;
import com.impinge.soul.activities.DashboardActivity;
import com.impinge.soul.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class NotificationService extends FirebaseMessagingService {
    public static String NOTIFICATION_ID = "notify_id";

    Intent i;
    private static int count = 0;
    PendingIntent pendingIntent;
    String play_list, user_id, subtype;


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    public static String getFCMDeviceToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);

            Log.e("P-Message", "remoteMessage == " + remoteMessage);
            Log.e("P-Message", "params == " + params);
            Log.e("P-Message", "object == " + object);

        } catch (Exception e) {
            e.printStackTrace();
        }


        String type = remoteMessage.getData().get("type");
        String message = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");


        // if (type.equalsIgnoreCase("music_alarm_notification")) {
        try {
            JSONObject object = new JSONObject(message);
            play_list = object.getString("songsPlayList");
            user_id = object.getString("user_id");
            subtype = object.getString("subtype");
            Log.e("notification_data", type + "----" + message + "------" + title + "------" + play_list + "----" + user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  }

        if (type.equalsIgnoreCase("music_alarm_notification")) {
            sendNotification(title, " ", type, "", play_list, subtype);
        } else if (type.equalsIgnoreCase("music_duration_notification")) {
            sendNotification(title, " ", type, "", play_list, subtype);
        } else {
            sendNotification(title, message, type, "", play_list, subtype);
        }


    }


    private void sendNotification(String title, String messageBody, String type, String notification_id, String play_list, String subtype) {

        if (type != null) {


            if (type.equalsIgnoreCase("Match")) {

                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pushnotification", "message");
                intent.putExtra("title", title);
                intent.putExtra("body", messageBody);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


            } else if (type.equalsIgnoreCase("thumbsUp")) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pushnotification", "thumbsUp");
                if (!notification_id.equals("")) {
                    intent.putExtra(NOTIFICATION_ID, notification_id);
                }
                intent.putExtra("title", title);
                intent.putExtra("body", messageBody);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            } else if (type.equalsIgnoreCase("music_alarm_notification")) {


                Intent intent = new Intent(getApplicationContext(), AudioPlayerViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("music_alarm_notification", "message");
                intent.putExtra("title", title);
                intent.putExtra("body", messageBody);
                intent.putExtra("album_id", play_list);
                if (subtype.equalsIgnoreCase("music_alarm_notification_start")) {
                    intent.setAction("start");

                } else if (subtype.equalsIgnoreCase("music_alarm_notification_end")) {
                    intent.setAction("stop");
                }
                intent.putExtra("subtype", subtype);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            } else if (type.equalsIgnoreCase("music_duration_notification")) {

                Intent intent = new Intent(getApplicationContext(), AudioPlayerViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("music_duration_notification", "message");
                intent.putExtra("title", title);
                intent.putExtra("body", messageBody);
                intent.putExtra("album_id", play_list);
                intent.putExtra("album_id", play_list);
                intent.setAction("stop");
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            }
           Uri uri = Uri.parse("android.resource://"
                    + getPackageName() + "/" + R.raw.sample3);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//For Android Version Orio and greater than orio.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel("Sesame", "Sesame", importance);
                mChannel.setDescription(messageBody);
                mChannel.enableLights(true);

                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)


                        .build();
                Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.test1);  //Here is FILE_NAME is the name of file that you want to play

                mChannel.setSound(defaultSoundUri, att);
                //mChannel.setSound(uri, att);

                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotifyManager.createNotificationChannel(mChannel);
            }
//For Android Version lower than oreo.
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Seasame");
                mBuilder.setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentText(messageBody)
                        .setSmallIcon(Utility.getNotificationIcon())
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#FFD600"))
                        .setContentIntent(pendingIntent)
                        .setChannelId("Sesame")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);


                mNotifyManager.notify(count, mBuilder.build());
                count++;

        }

    }

}
