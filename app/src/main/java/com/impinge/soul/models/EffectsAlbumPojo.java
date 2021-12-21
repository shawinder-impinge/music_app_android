package com.impinge.soul.models;

import java.util.ArrayList;

public class EffectsAlbumPojo {


    public int id;
    public int listen_count;
    public String name;

    public String cover_images;
    public String description;
    public boolean status;
    public ArrayList<songsPojo> effect_songs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListen_count() {
        return listen_count;
    }

    public void setListen_count(int listen_count) {
        this.listen_count = listen_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_images() {
        return cover_images;
    }

    public void setCover_images(String cover_images) {
        this.cover_images = cover_images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<songsPojo> getEffect_songs() {
        return effect_songs;
    }

    public void setEffect_songs(ArrayList<songsPojo> effect_songs) {
        this.effect_songs = effect_songs;
    }
}
