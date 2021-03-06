package com.impinge.soul.models;

import java.util.ArrayList;

public class CategoryPojo {

    private int id;
    private String name;
    private String title;
    private String description;
    private String cover_images;
    private String songs_length;
    private int listen_count;
    private boolean status;
    private ArrayList<ImagePojo> images;
    private ArrayList<songsPojo> songs;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover_images() {
        return cover_images;
    }

    public void setCover_images(String cover_images) {
        this.cover_images = cover_images;
    }

    public String getSongs_length() {
        return songs_length;
    }

    public void setSongs_length(String songs_length) {
        this.songs_length = songs_length;
    }

    public int getListen_count() {
        return listen_count;
    }

    public void setListen_count(int listen_count) {
        this.listen_count = listen_count;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<ImagePojo> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagePojo> images) {
        this.images = images;
    }

    public ArrayList<songsPojo> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<songsPojo> songs) {
        this.songs = songs;
    }
}
