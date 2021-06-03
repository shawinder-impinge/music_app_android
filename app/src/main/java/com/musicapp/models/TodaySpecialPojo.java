package com.musicapp.models;

import java.util.ArrayList;

public class TodaySpecialPojo {

    private int id;
    private String name;
    private String title;
    private boolean status;
   /* private ArrayList<VideoPojo> video;*/
    private VideoPojo video;
    private String listen_count;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public VideoPojo getVideo() {
        return video;
    }

    public void setVideo(VideoPojo video) {
        this.video = video;
    }

    public String getListen_count() {
        return listen_count;
    }

    public void setListen_count(String listen_count) {
        this.listen_count = listen_count;
    }
}
