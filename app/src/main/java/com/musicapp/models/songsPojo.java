package com.musicapp.models;

import java.util.ArrayList;

public class songsPojo {

    private int id;
    private String name;
    private String title;
    private String audios;
    private String description;
    private String songs_length;
    private String cover_images;
    private String author_name;
    private String author_image;

    public String getCover_images() {
        return cover_images;
    }

    public void setCover_images(String cover_images) {
        this.cover_images = cover_images;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    private int listen_count;
    private boolean is_user_fav;
    private ArrayList<ImagePojo> images;

    public boolean isIs_user_fav() {
        return is_user_fav;
    }

    public void setIs_user_fav(boolean is_user_fav) {
        this.is_user_fav = is_user_fav;
    }

    public String getSongs_length() {
        return songs_length;
    }

    public void setSongs_length(String songs_length) {
        this.songs_length = songs_length;
    }

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

    public String getAudios() {
        return audios;
    }

    public void setAudios(String audios) {
        this.audios = audios;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getListen_count() {
        return listen_count;
    }

    public void setListen_count(int listen_count) {
        this.listen_count = listen_count;
    }

    public ArrayList<ImagePojo> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagePojo> images) {
        this.images = images;
    }
}
