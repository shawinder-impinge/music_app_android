package com.impinge.soul.models;

import java.io.Serializable;

public class ExploreVideos implements Serializable {
    String id,name,file,duration;
    boolean is_user_fav;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isIs_user_fav() {
        return is_user_fav;
    }

    public void setIs_user_fav(boolean is_user_fav) {
        this.is_user_fav = is_user_fav;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
