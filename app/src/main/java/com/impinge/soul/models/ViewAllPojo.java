package com.impinge.soul.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewAllPojo implements Serializable {

    ArrayList<songsPojo> songs;

    public ArrayList<songsPojo> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<songsPojo> songs) {
        this.songs = songs;
    }
}
