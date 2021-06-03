package com.musicapp.models;

public class TimeModal {
    String time,rocord_time_id;

    public TimeModal(String time, String rocord_time_id) {
        this.time = time;
        this.rocord_time_id = rocord_time_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRocord_time_id() {
        return rocord_time_id;
    }

    public void setRocord_time_id(String rocord_time_id) {
        this.rocord_time_id = rocord_time_id;
    }
}
