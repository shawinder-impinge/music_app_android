package com.impinge.soul.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchPojo implements Serializable {
    ArrayList<SearchSongModel> songs;

    public ArrayList<SearchSongModel> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<SearchSongModel> songs) {
        this.songs = songs;
    }
}
