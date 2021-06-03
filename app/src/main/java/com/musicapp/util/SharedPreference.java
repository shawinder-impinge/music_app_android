package com.musicapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

    public class SharedPreference {
        private static SharedPreferences sharedPreference;
        private static SharedPreferences.Editor editor;
        private static final String AUTH_DATA="AUTH_DATA";


        public static void storeData(Context context, String key, String value) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.putString(key, value);
            editor.commit();
        }

        public static String retrieveData(Context context, String Key) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            return sharedPreference.getString(Key, null);
        }

        public static void removeAll(Context context) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.clear();
            editor.commit();
        }

        public static void removeKey(Context context, String Key) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.remove(Key);
            editor.commit();
        }

        public static void storeAndParseJsonData(Context context, JSONObject object)
        {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            try {
                editor.putString(Constants.Id,object.getString("id"));
                editor.putString(Constants.Name,object.getString("name"));
                editor.putString(Constants.User_type,object.getString("user_type"));
                editor.putString(Constants.Email,object.getString("email"));
                editor.putString(Constants.Image,object.getString("image"));
                editor.putString(Constants.Country_id,object.getString("country_id"));
                editor.putString(Constants.Age,object.getString("age"));
                editor.putString(Constants.Gender,object.getString("gender"));
                editor.putString(Constants.smoking,object.getString("smoking"));
                editor.putString(Constants.residence,object.getString("residence"));
                editor.putString(Constants.region,object.getString("region"));
                editor.putString(Constants.social_status,object.getString("social_status"));
                editor.putString(Constants.marriage_type,object.getString("marriage_type"));
                editor.putString(Constants.children,object.getString("children"));
                editor.putString(Constants.Height,object.getString("Height"));
                editor.putString(Constants.weight,object.getString("weight"));
                editor.putString(Constants.skin_color,object.getString("skin_color"));
                editor.putString(Constants.hair_color,object.getString("hair_color"));
                editor.putString(Constants.body_type,object.getString("body_type"));
                editor.putString(Constants.LAT,String.valueOf(object.getString("latitude")));
                editor.putString(Constants.LONG,String.valueOf(object.getString("longitude")));
                editor.putString(Constants.free_trialss,object.getString("free_trial"));
                editor.putString(Constants.free_trial_status,object.getString("free_trial_status"));
                editor.putString(Constants.Pasword,object.getString("decoded_password"));


                if (!object.has("aboutMe"))
                {
                    editor.putString(Constants.Introduction,object.getString("introduction"));
                    editor.putString(Constants.Looking_for,object.getString("looking_for"));
                }else if (!object.has("country"))
                {
                    editor.putString(Constants.country_name,object.getString("c_name"));
                    editor.putString(Constants.flag,object.getString("flag"));
                    editor.putString(Constants.search_parallelity,object.getString("search_parallelity"));

                }
                editor.putString(Constants.Facebuk,object.getString("facebook_id"));
                editor.putString(Constants.facebook_status,object.getString("fb_status"));




            } catch (JSONException e) {
                e.printStackTrace();
            }
            editor.commit();
        }

        /*To Store Fcm Device ID*/
        public static void storeFcmDeviceId(Context context, String val) {
            sharedPreference = context.getSharedPreferences("FCM", Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.putString("DEVICE", val);
            editor.commit();
        }

        public static String retrieveFcmDeviceId(Context context) {
            sharedPreference = context.getSharedPreferences("FCM", Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            return sharedPreference.getString("DEVICE", null);
        }

        public static void storeNotificationCount(Context context, String key, int value) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.putInt(key, value);
            editor.commit();
        }

        public static int retrieveNotificationCount(Context context, String Key) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            return sharedPreference.getInt(Key, 0);
        }

        public static void removeNotificationCount(Context context, String Key) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.remove(Key);
            editor.commit();
        }


        public static String storeEnglish(Context context, String key) {
            sharedPreference = context.getSharedPreferences("en", Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            return sharedPreference.getString("en", null);
        }



        public static String retriveEnglish(Context context,String key) {
            sharedPreference = context.getSharedPreferences("en", Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            return sharedPreference.getString("en", null);
        }




        public static String storeArabic(Context context,String key) {
            sharedPreference = context.getSharedPreferences("ar", Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            return sharedPreference.getString("ar", null);
        }



        public static String retriveArabic(Context context,String key) {
            sharedPreference = context.getSharedPreferences("ar", Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            return sharedPreference.getString("ar", null);
        }


        public static void storeDataEnglish(Context context, String key, String value) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.putString(key, value);
            editor.commit();
        }

        public static String retrieveDataenglish(Context context, String Key)
        {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            return sharedPreference.getString(Key, null);
        }

        public static void storeDataarabic(Context context, String key, String value) {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.putString(key, value);
            editor.commit();
        }

        public static String retrieveDataarabic(Context context, String Key)
        {
            sharedPreference = context.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE);
            return sharedPreference.getString(Key, null);
        }

        public static void savePreferenceData(Context context,String Key, String val) {
            sharedPreference = context.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.putString(Key, val);
            editor.commit();
        }



        public static String fetchPrefenceData(Context context,String key) {
            sharedPreference = context.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            return sharedPreference.getString(key, "");
        }

    }


