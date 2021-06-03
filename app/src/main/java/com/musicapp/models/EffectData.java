package com.musicapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class EffectData implements Serializable {

    ArrayList<songsPojo> songs;
    ArrayList<ExploreVideos> videos;

    public ArrayList<ExploreVideos> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<ExploreVideos> videos) {
        this.videos = videos;
    }

    public ArrayList<songsPojo> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<songsPojo> songs) {
        this.songs = songs;
    }
}
