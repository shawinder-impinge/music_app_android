package com.musicapp.util;

public class PreferenceData {

    public static final String USER_ID= "id";
    public static final String TOKEN= "token";
    public static final String USERNAME= "username";
    public static final String LOGIN_TYPE_USED= "LOGIN_TYPE_USED";

    public static enum LOGIN_TYPE{
        FACEBOOK,
        GMAIL,
        STANDARD
    }
}
